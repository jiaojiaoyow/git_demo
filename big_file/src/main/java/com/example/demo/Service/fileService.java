package com.example.demo.Service;

import com.example.demo.Param.fileParam;

import java.io.IOException;

/**
 * 存储操作的service
 */
public interface fileService {
    /**
     * 删除全部数据
     */
    void deleteAll();

    /**
     * 初始化方法
     */
    void init();

    /**
     * 上传文件
     * 处理文件分块，基于mappedByteBuffer来实现
     */
    void uploadFileByMappedByteBuffer(fileParam fileParam) throws IOException;
}
