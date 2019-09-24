package com.neu.t10.util;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;


public class UserRegisteSalt {
    public static String encryptPassword(String password,String salt){

        String afterPass=new Md5Hash(password,ByteSource.Util.bytes(salt),2).toString();
        return afterPass;
    }


}
