package com.neu.t10.Controller;

import com.neu.t10.config.RetryLimitHashedCredentialsMatcher;
import com.neu.t10.entity.SysUser;
import com.neu.t10.util.CaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher;

    public static final String KEY_CAPTCHA = "KEY_CAPTCHA";

    @RequestMapping("toLogin")
    public String tologin(){
        return "login";
    }

    @RequestMapping("login")
    public String login(HttpServletRequest request, Model model, HttpSession session, SysUser sysUser,String captcha,boolean rememberMe){

        //校验验证码
        //session中的验证码
        String sessionCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(LoginController.KEY_CAPTCHA);
        if (null == captcha || !captcha.equalsIgnoreCase(sessionCaptcha)) {
            model.addAttribute("msg","验证码错误！");
            return "login";
        }

        //没有配置记住我的
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(sysUser.getUsername(),sysUser.getPassword());
        //配置了记住我的
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(sysUser.getUsername(),sysUser.getPassword(),rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            //登录操作
            //对密码进行加密
            //sysUser.setPassword(new SimpleHash("md5", sysUser.getPassword(), ByteSource.Util.bytes(sysUser.getCredentialsSalt()),2).toHex());
            subject.login(usernamePasswordToken);
            SysUser user=(SysUser) subject.getPrincipal();
            //更新用户登录时间，也可以在ShiroRealm里面做
            session.setAttribute("user", user);
            return "redirect:/userList";
        } catch(Exception e) {
            //登录失败从request中获取shiro处理的异常信息 shiroLoginFailure:就是shiro异常类的全类名
            String exception = (String) request.getAttribute("shiroLoginFailure");
            model.addAttribute("msg",e.getMessage());
            //返回登录页面
            return "login";
        }
    }

    @RequestMapping("403")
    public String unauthrizedRole(){
        System.out.println("没有权限");
        return "403";
    }

    /**
     * 解除admin 用户的限制登录
     * 写死的 方便测试
     * @return
     */
    @RequestMapping("unlockAccount")
    public String unlockAccount(Model model){
        model.addAttribute("msg","用户解锁成功");

        retryLimitHashedCredentialsMatcher.unlockAccount("admin");

        return "login";
    }


    /**
     * 获取验证码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("Captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // 设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        // 不缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        try {

            HttpSession session = request.getSession();

            CaptchaUtil tool = new CaptchaUtil();
            StringBuffer code = new StringBuffer();
            BufferedImage image = tool.genRandomCodeImage(code);
            session.removeAttribute(KEY_CAPTCHA);
            session.setAttribute(KEY_CAPTCHA, code.toString());

            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
