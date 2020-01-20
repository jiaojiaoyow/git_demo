package com.example.demo;

import com.example.demo.entity.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Resource
    com.example.demo.dao.uploadDao uploadDao;
    @Test
    public void contextLoads() {
        File file = new File();
        file.setFileName("111");
        file.setFilePath("222");
        file.setFileSize(555);
        file.setMd5value("555555");
        System.out.println(uploadDao.insertFileInfo(file));
    }
    @Test
    public void test(){
        System.out.println(uploadDao.selectIdBymd5value("555555"));
    }
}
