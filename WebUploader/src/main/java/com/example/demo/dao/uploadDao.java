package com.example.demo.dao;

import com.example.demo.entity.File;
import com.example.demo.entity.Img;
import com.example.demo.entity.User;

import java.util.List;

public interface uploadDao {
    public int insertFileInfo(File file);
    public Object selectIdBymd5value(String md5value);
    public int insertUserInfo(User user);
    public List<File> selectFile();
    public File selectFileById(int id);
    public int insertImgInfo(Img img);
    public List<Img> selectImg();
}
