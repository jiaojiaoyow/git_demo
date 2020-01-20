package com.example.demo.Controller;

import com.example.demo.Param.fileParam;
import com.example.demo.Service.Impl.fileServieImpl;
import com.example.demo.Utils.constants;
import com.example.demo.Vo.ResultStatus;
import com.example.demo.Vo.ResultVo;
import com.sun.jnlp.FileOpenServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/index")
public class fileController {
    //引入redis
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private fileServieImpl fileService;


    /**
     * 妙传判断，断点判断
     */
    @RequestMapping("checkFileMd5")
    public Object checkFileMD5(String md5) throws IOException{
        //取键值为状态和md5的值
        Object processingObj=stringRedisTemplate.opsForHash().get(constants.FILE_UPLOAD_STATUS,md5);
        if(processingObj==null){
            //返回文件不存在，没上传过
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr=processingObj.toString();
        boolean processing=Boolean.parseBoolean(processingStr);
        String value=stringRedisTemplate.opsForValue().get(constants.FILE_MD5_KEY+md5);
        if(processing){
            //已经上传完毕
            return new ResultVo(ResultStatus.IS_HAVE,value);
        }else {
            //还没上传完毕，继续上传
            File confFile = new File(value);
            byte[] completeList= FileUtils.readFileToByteArray(confFile);
            List<String> missChunkList= new LinkedList<>();
            for (int i = 0; i <completeList.length ; i++) {
                if(completeList[i]!=Byte.MAX_VALUE){
                    missChunkList.add(i+"");
                }
            }
            return new ResultVo<>(ResultStatus.ING_HAVE,missChunkList);
        }
    }


    /**
     * 上传文件
     */
    @RequestMapping("/fileUpload")
    public ResponseEntity fileUpload(fileParam param , HttpServletRequest request){
        boolean isMultipart= ServletFileUpload.isMultipartContent(request);
        if(isMultipart){
            System.out.println("开始上传");
            try{
                fileService.uploadFileByMappedByteBuffer(param);
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("结束上传");
        }
        return ResponseEntity.ok().body("上传成功");
    }



}
