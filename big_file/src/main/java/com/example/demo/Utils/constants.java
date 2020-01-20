package com.example.demo.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量表
 */
public class constants {
    /**
     * 异常信息统一头信息
     */
    public static final String Exception_hand="出错了";

    /**
     * 缓存键值
     */
    public static final Map<Class<?>,String> chcheKeyMap=new HashMap<>();

    /**
     * 保存文件所在路径的key
     */
    public static final String FILE_MD5_KEY="FILE_MD5";

    /**
     * 保存上传文件的状态
     */
    public static final String FILE_UPLOAD_STATUS="FILE_UPLOAD_STATUS";
}
