package com.example.demo.controller;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.demo.entity.File;
import com.example.demo.entity.ResultObj;
import com.example.demo.entity.User;
import com.example.demo.service.uploadService;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/bigFile")
public class BigFileUploadController {
    @Resource
    uploadService uploadService;
    private String uploadPath=getUploadPath();

    // 通过MD5查找文件信息，如果有的话，就不用上传文件，只要向用户文件里插入文件信息就行
    @GetMapping("/findResourceFileExistsByMd5")
    public Object findMd5(String md5value, String fileName, String caozuoPath, HttpSession session)
            throws Exception {
        Timestamp uploadTime = new Timestamp(System.currentTimeMillis());
        // 如果数据里有MD5，就不用上传，但是用户记录需要插入。
        Object fileId = uploadService.selectIdBymd5value(md5value);
        if(fileId!=null){
            User user = new User();
            user.setUserId("1");
            user.setFileId((int)fileId);
            uploadService.insertUserInfo(user);
            return new ResultObj(200, fileId);
        }else{
            return new ResultObj(400, 3);
        }
    }

    //这里通过文件编号guid和分片数chunk来检查分片文件，其实严格一点还需要检查分片文件的偏移位置和文件大小，以防止分片文件上传失败的情况，这里表示一下就行，需要自己改改
    @GetMapping("/findResourceChunkExists")
    public ResultObj findResourceChunkExists(String guid,Integer chunk){

        // 临时目录用来存放所有分片文件
        String tempFileDir = uploadPath+ "/bigFileTemp/" + guid;
        java.io.File parentFileDir = new java.io.File(tempFileDir);
        java.io.File tempPartFile = new java.io.File(parentFileDir, guid + "_" + chunk + ".part");
        if(tempPartFile.exists()) {
            return new ResultObj(200, "存在");
        }else {
            return new ResultObj(404, "不存在");
        }
    }

    // 如果前面的MD5没有找到文件信息的话，就会返回一个400到前端，前端再根据判断，把文件给上传,文件上传完成后，才会到这一步。
    @PostMapping("/saveFileInfo")
    public ResultObj saveFileInfo(String guid, String md5value, Long fileSize, String fileName,
                        String caozuoPath, HttpSession session, HttpServletRequest req) throws Exception {
        Timestamp uploadTime = new Timestamp(System.currentTimeMillis());

        System.out.println("saveFileInfo：");
        System.out.println("------------guid："+guid);
        System.out.println("------------md5value："+md5value);
        System.out.println("------------fileSize："+fileSize);
        System.out.println("------------fileName："+fileName);
        System.out.println("------------caozuoPath："+caozuoPath);
        String newName = fileName;

        java.io.File dest = mergeFile(guid, newName);

        File file = new File();
        file.setFilePath(dest.getPath());
        file.setFileName(fileName);
        file.setFileSize(fileSize);
        file.setMd5value(md5value);
        uploadService.insertFileInfo(file);
        Object fileId = uploadService.selectIdBymd5value(md5value);
        //用户角色
        User user = new User();
        user.setUserId("1");
        user.setFileId((int)fileId);
        uploadService.insertUserInfo(user);

        return new ResultObj(200, file);
    }

    /**
     * 上传文件
     * @param request
     * @param response
     * @param guid
     * @param chunk
     * @param file
     * @param chunks
     */
    @RequestMapping("/upload")
    public void bigFile(HttpServletRequest request, HttpServletResponse response,String guid,Integer chunk, MultipartFile file,Integer chunks){
        try {
            //判断是普通表单，还是带文件上传的表单
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            if (isMultipart) {
                // 临时目录用来存放所有分片文件
                String tempFileDir = uploadPath+ "/bigFileTemp/" + guid;
                java.io.File parentFileDir = new java.io.File(tempFileDir);
                if (!parentFileDir.exists()) {
                    parentFileDir.mkdirs();
                }
                // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                java.io.File tempPartFile = new java.io.File(parentFileDir, guid + "_" + chunk + ".part");
                FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并文件
     * @param guid
     * @param fileName
     * @throws Exception
     */
    public java.io.File mergeFile(String guid, String fileName){
        // 得到 destTempFile 就是最终的文件
        try {
            java.io.File parentFileDir = new java.io.File(uploadPath+ "/bigFileTemp/" + guid);
            if(parentFileDir.isDirectory()){

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 转换为字符串
                String formDate = format.format(new Date());
                java.io.File destTempFile = new java.io.File(uploadPath, fileName);
                System.out.println(destTempFile.getPath());

                if(!destTempFile.exists()){
                    //先得到文件的上级目录，并创建上级目录，在创建文件,
                    destTempFile.getParentFile().mkdirs();
                    try {
                        //创建文件
                        destTempFile.createNewFile(); //上级目录没有创建，这里会报错
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //根据分片文件夹的文件数量来判断有多少个文件需要整合
                System.out.println(parentFileDir.listFiles().length);
                for (int i = 0; i < parentFileDir.listFiles().length; i++) {
                    java.io.File partFile = new java.io.File(parentFileDir, guid + "_" + i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
                    //遍历"所有分片文件"到"最终文件"中
                    FileUtils.copyFile(partFile, destTempfos);
                    destTempfos.close();
                }
                // 删除临时目录中的分片文件
                FileUtils.deleteDirectory(parentFileDir);
                return destTempFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public String getUploadPath(){
        //获取项目路径
        String uploadPath="";
        try {
            java.io.File path = new java.io.File(ResourceUtils.getURL("").getPath());
            java.io.File upload = new java.io.File(URLDecoder.decode(path.getAbsolutePath()),"/upload/");
            if(!upload.exists()) upload.mkdirs();
            uploadPath = URLDecoder.decode(upload.getAbsolutePath(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadPath;
    }

    /**
     * 大文件下载
     * @param response
     * @param choice
     * @throws Exception
     */
    @RequestMapping("download")
    public void download(HttpServletResponse response,String[] choice) throws Exception {
        //获取项目路径
        java.io.File path = new java.io.File(ResourceUtils.getURL("").getPath());

        for (int i=0;i<choice.length;i++){
            // 文件地址
            java.io.File file = new java.io.File(URLDecoder.decode(path.getAbsolutePath()),"/upload/"+choice[0]);

            // 文件输入对象
            FileInputStream fis = new FileInputStream(file);
            // 设置相关格式
            response.setContentType("application/force-download");
            // 设置下载后的文件名以及header
            response.addHeader("Content-disposition", "attachment;fileName=" + choice[0]);
            // 创建输出对象
            OutputStream os = response.getOutputStream();
            // 常规操作
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            fis.close();
        }
    }
}