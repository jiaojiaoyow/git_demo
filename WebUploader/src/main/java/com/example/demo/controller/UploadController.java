package com.example.demo.controller;
import com.example.demo.entity.Img;
import com.example.demo.entity.User;
import com.example.demo.service.uploadService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {
    @Resource
    uploadService uploadService;
    @RequestMapping(value = "/uploader",method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        MultipartHttpServletRequest Murequest = (MultipartHttpServletRequest)request;
        Map<String, MultipartFile> files = Murequest.getFileMap();//得到文件map对象

        //获取项目路径
        File path = new File(ResourceUtils.getURL("").getPath());
        File upload = new File(URLDecoder.decode(path.getAbsolutePath()),"/upload/img/");
        if(!upload.exists()) upload.mkdirs();
        String upaloadUrl = URLDecoder.decode(upload.getAbsolutePath(),"utf-8");

        File dir = new File(upaloadUrl);

        Integer counter=0;
        if(!dir.exists())//目录不存在则创建
            dir.mkdirs();
        for(MultipartFile file :files.values()){
            counter++;
            //获取文件名
            String fileName=file.getOriginalFilename();

            // 获取扩展名
            String fileExt = fileName.substring(
                    fileName.lastIndexOf(".") + 1).toLowerCase();

            //文件名 ：原文件名+随机数
            String newFileName = fileName.substring(0,fileName.lastIndexOf("."))+ "_"
                    + new Random().nextInt(1000) + "." + fileExt;

            File tagetFile = new File(upaloadUrl+"\\"+newFileName);//创建文件对象

            Img img = new Img();
            img.setImgName(newFileName);
            img.setImgPath(tagetFile.getPath());
            uploadService.insertImgInfo(img);

            if(!tagetFile.exists()){//文件名不存在 则新建文件，并将文件复制到新建文件中
                tagetFile.createNewFile();
                file.transferTo(tagetFile);
            }
        }
    }

    @RequestMapping(value = "/downFile")
    public ResponseEntity<byte[]> fileDownload(HttpServletRequest request,
                                               String[] choice) throws Exception{
        //获取下载文件路径
        File paths = new File(ResourceUtils.getURL("").getPath());
        File upload = new File(URLDecoder.decode(paths.getAbsolutePath()),"/upload/img/");
        if(!upload.exists()) upload.mkdirs();
        String path = URLDecoder.decode(upload.getAbsolutePath(),"utf-8");

        //需要压缩的文件
        List<String> list = new ArrayList<String>();

        for(int i = 0;i<choice.length;i++) {
            list.add(choice[i]);
        }

        //压缩后的文件
        String resourcesName = "图片.zip";
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path+"/"+resourcesName));//压缩包路径
        InputStream input = null;

        for (String str : list) {
            String name = path+"/"+str;
            input = new FileInputStream(new File(name));
            zipOut.putNextEntry(new ZipEntry(str));
            int temp = 0;
            while((temp = input.read()) != -1){
                zipOut.write(temp);
            }
            input.close();
        }
        zipOut.close();
        File file = new File(path+"/"+resourcesName);
        HttpHeaders headers = new HttpHeaders();
        resourcesName = this.getFilename(request, resourcesName);
        headers.setContentDispositionFormData("attachment", resourcesName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.OK);
    }

    /**
     * 根据浏览器的不同进行编码设置，返回编码后的文件名
     */
    public String getFilename(HttpServletRequest request, String filename) throws Exception {
        // IE不同版本User-Agent中出现的关键词
        String[] IEBrowserKeyWords = { "MSIE", "Trident", "Edge" };
        // 获取请求头代理信息
        String userAgent = request.getHeader("User-Agent");
        for (String keyWord : IEBrowserKeyWords) {
            if (userAgent.contains(keyWord)) {
                // IE内核浏览器，统一为UTF-8编码显示
                return URLEncoder.encode(filename, "UTF-8");
            }
        }
        // 火狐等其它浏览器统一为ISO-8859-1编码显示
        return new String(filename.getBytes("UTF-8"), "ISO-8859-1");
    }
}