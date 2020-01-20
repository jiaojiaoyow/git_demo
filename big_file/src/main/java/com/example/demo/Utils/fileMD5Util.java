package com.example.demo.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;

/**
 * 获取文件的md5
 */
public class fileMD5Util {
    //通过FileInputStream读取文件的信息
    public static String getFileMD5(File file)throws FileNotFoundException {
        String value=null;
//        新建文件输入流
        FileInputStream in=new FileInputStream(file);

        MappedByteBuffer btyeBuffer=null;
        try{
            btyeBuffer=in.getChannel().map(FileChannel.MapMode.READ_ONLY,0,file.length());
            /**
             * MessageDigest类为应用程序提供消息摘要算法的功能，如SHA-1或SHA-256。
             *  消息摘要是采用任意大小的数据并输出固定长度散列值的安全单向散列函数。
             */
            MessageDigest md5=MessageDigest.getInstance("MD5");
            md5.update(btyeBuffer);
            BigInteger bi=new BigInteger(1,md5.digest());
            value=bi.toString(16);
            if(value.length()<32){
                value="0"+value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in!=null){
                try{
                    //关闭
                    in.getChannel().close();
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (btyeBuffer!=null) {
                freedMappedByteBuffer(btyeBuffer);
            }
        }
        return value;
    }

    /**
     * 在MappedByteBuffer释放后再对他进行读操作的话，就会引发jvm crash，在并发环境下很容易发生
     * 正在释放时另外一个线程正开始读取，于是就发生了异常，所以为了提高系统稳定性，释放时需要判断是否有线程在读或者写
     */
    public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer){
        try {
            if(mappedByteBuffer==null){
                return;
            }

            //将缓冲区的内容强制写入映射文件的存储设备中
            mappedByteBuffer.force();

            /**
             * https://blog.csdn.net/yekong1225/article/details/81011819
             * 将代码标记为享有“特权
             */

            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    //
                    try{
                        //找到方法
                        Method getCleanerMethod=mappedByteBuffer.getClass().getMethod("cleaner",new Class[0]);
                        //设置方法可以访问
                        getCleanerMethod.setAccessible(true);
                        //执行方法
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer,
                                new Object[0]);
                        cleaner.clean();
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("clean MappedBtyeBuffer error");
                    }
                    System.out.println("clean Mapped completed");
                    return null;
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
