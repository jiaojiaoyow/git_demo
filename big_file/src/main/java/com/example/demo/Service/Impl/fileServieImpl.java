package com.example.demo.Service.Impl;

import com.example.demo.Param.fileParam;
import com.example.demo.Service.fileService;
import com.example.demo.Utils.constants;
import com.example.demo.Utils.fileMD5Util;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class fileServieImpl implements fileService {

    //文件的根目录
    private Path rootPath;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //这个必须与前端设定的值一致
    @Value("${breakpoint.upload.chunkSize}")
    private long CHUNK_SIZE;


    @Value("${breakpoint.upload.dir}")
    private String finalDirPath;

    @Autowired
    public fileServieImpl(@Value("${breakpoint.upload.dir}") String location) {
        this.rootPath = Paths.get(location);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootPath.toFile());
        stringRedisTemplate.delete(constants.FILE_UPLOAD_STATUS);
        stringRedisTemplate.delete(constants.FILE_MD5_KEY);
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootPath);
        } catch (FileAlreadyExistsException e) {
            System.out.println("文件夹已经存在了，不用再创建。");
        } catch (IOException e) {
            System.out.println("初始化root文件夹失败。");
        }
    }

    @Override
    public void uploadFileByMappedByteBuffer(fileParam fileParam) throws IOException {
        //基础信息
        String fileName=fileParam.getName();
        String uploadDirPath=finalDirPath+fileParam.getMd5();
        //设置临时文件名
        String tempFileName=fileName+"_tmp";

        //文件目录
        File tmpDir=new File(uploadDirPath);
        File tmpFile=new File(uploadDirPath,tempFileName);

        //如果不存在该目录，则创建
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }

        //通过randomAccessFile获取管道
        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        //开始写入分片数据
        Long offset=CHUNK_SIZE*fileParam.getChunk();
        byte[] fileData=fileParam.getFile().getBytes();

        //通过map（）将buffer映射到文件
        MappedByteBuffer mappedByteBuffer=fileChannel.map(FileChannel.MapMode.READ_WRITE,offset,fileData.length);

        //写入数据
        mappedByteBuffer.put(fileData);

        //释放缓冲区，关闭管道
        fileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();

        //检查文件上传进度
        boolean isOk = checkAndSetUploadProgress(fileParam, uploadDirPath);
        if (isOk) {
            boolean flag = renameFile(tmpFile, fileName);
            System.out.println("upload complete !!" + flag + " name=" + fileName);
        }
    }

    /**
     * 检查并修改文件上传进度
     */
    private boolean checkAndSetUploadProgress(fileParam param , String uploadDirPath) throws IOException{
        String fileName=param.getName();
        File confFile=new File(uploadDirPath,fileName+".conf");
        RandomAccessFile accessConfFile=new RandomAccessFile(confFile,"rw");
        //把该分段标记为 true 表示完成
        System.out.println("set part " + param.getChunk() + " complete");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList是用来遍历，检查文件是否全部完成，如果数组是全部都为是，则上传成功
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete= Byte.MAX_VALUE;
        for (int i = 0; i <completeList.length&&isComplete==Byte.MAX_VALUE ; i++) {
            //与运算，如果有部分没有完成则isComplete不是Byte.MAX_VALUE
            isComplete=(byte)(isComplete&completeList[i]);
            System.out.println("check part"+i+"complete?:"+completeList[i]);
        }

        accessConfFile.close();

        //放入redis
        if(isComplete==Byte.MAX_VALUE){
            stringRedisTemplate.opsForHash().put(constants.FILE_UPLOAD_STATUS,param.getMd5(),"true");
            stringRedisTemplate.opsForValue().set(constants.FILE_MD5_KEY+param.getMd5(),uploadDirPath+"/"+fileName);
        }else {
            if (!stringRedisTemplate.opsForHash().hasKey(constants.FILE_UPLOAD_STATUS, param.getMd5())) {
                stringRedisTemplate.opsForHash().put(constants.FILE_UPLOAD_STATUS, param.getMd5(), "false");
            }
            if (stringRedisTemplate.hasKey(constants.FILE_MD5_KEY + param.getMd5())) {
                stringRedisTemplate.opsForValue().set(constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName + ".conf");
            }
            return false;
        }
        return true;
    }


    /**
     * 文件重命名
     *
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @return
     */
    public boolean renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            System.out.println("文件不存在");
            return false;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        return toBeRenamed.renameTo(newFile);
    }
}





