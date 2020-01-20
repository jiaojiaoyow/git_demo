package com.example.demo.Param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class fileParam {

    //总分片数量
    private Integer chunks=0;
    //当前第几片分片
    private Integer chunk=0;
    //分片大小
    private Long size=0L;
    //文件md5值
    private String md5;
    //分片文件对象
    private MultipartFile file;
    /*
    文件基础信息
     */
    //文件名字
    private String name;
    //任务id
    private String id;
    //用户id
    private String uid;
}
