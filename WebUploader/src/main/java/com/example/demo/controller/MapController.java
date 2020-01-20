package com.example.demo.controller;

import com.example.demo.entity.File;
import com.example.demo.entity.Img;
import com.example.demo.service.uploadService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;

@Controller
public class MapController {
    @Resource
    uploadService uploadService;

    @RequestMapping(value = "/demo")
    public String demo(){
        return "demo";
    }
    @RequestMapping(value = "/fpdemo")
    public String fpdemo(){
        return "fpdemo";
    }
    @RequestMapping(value = "/download")
    public String download(Model model){
        List<File> fileList = uploadService.selectFile();
        DecimalFormat df = new DecimalFormat("0.0");
        for (File file : fileList) {
            file.setFileSize(Double.parseDouble(df.format(file.getFileSize()/1024/1024)));
        }
        model.addAttribute("fileList",fileList);

        List<Img> imgList = uploadService.selectImg();
        model.addAttribute("imgList",imgList);
        return "download";
    }
}
