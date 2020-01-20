package com.example.demo.service;

import com.example.demo.dao.uploadDao;
import com.example.demo.entity.File;
import com.example.demo.entity.Img;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class uploadService {
    @Resource
    uploadDao uploadDao;
    public int insertFileInfo(File file){
        return uploadDao.insertFileInfo(file);
    }
    public Object selectIdBymd5value(String md5value){
        return uploadDao.selectIdBymd5value(md5value);
    }
    public int insertUserInfo(User user){
        return uploadDao.insertUserInfo(user);
    }
    public List<File> selectFile(){return uploadDao.selectFile();}
    public File selectFileById(int id){return uploadDao.selectFileById(id);}
    public int insertImgInfo(Img img){
        return uploadDao.insertImgInfo(img);
    }
    public List<Img> selectImg(){return uploadDao.selectImg();}
}
