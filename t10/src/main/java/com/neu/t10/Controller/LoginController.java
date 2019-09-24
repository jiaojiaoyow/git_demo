package com.neu.t10.Controller;

import com.neu.t10.entity.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/")
public class LoginController {
    @RequestMapping("toLogin")
    public String tologin(){
        return "login";
    }

    @RequestMapping("login")
    public String login(SysUser sysUser){
        //如果登陆不成功，就返回登陆页面
        return "login";
    }

    @RequestMapping("403")
    public String unauthrizedRole(){
        System.out.println("没有权限");
        return "403";
    }

}
