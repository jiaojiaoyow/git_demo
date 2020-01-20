package com.neu.t10.Controller;

import com.neu.t10.Realm.MyShiroRealm;
import com.neu.t10.dao.SysUserDao;
import com.neu.t10.entity.SysUser;
import com.neu.t10.util.UserRegisteSalt;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private SysUserDao sysUserDao;

    UserRegisteSalt userRegisteSalt=new UserRegisteSalt();


    //查看用户列表
    @RequestMapping("userList")
    //通过相应的权限
    @RequiresPermissions("sys:user:list,sys:user:info")
    //通过相应的角色来管理权限
//    @RequiresRoles("管理角色")
    public String userList(Model model){
        List<SysUser> users=sysUserDao.selectAll();
        model.addAttribute("users",users);
        return "manage";
    }


    //增加用户
    @RequestMapping("userSave")
    @RequiresPermissions("sys:user:save,sys:role:select")//通过权限认证

    public String userSave(Model model, SysUser sysUser){
        //注册的时候把密码加密
        String newPass=userRegisteSalt.encryptPassword(sysUser.getPassword(),sysUser.getCredentialsSalt());
        sysUser.setPassword(newPass);
        Long user_id=sysUserDao.insert(sysUser);
        List<SysUser> users=sysUserDao.selectAll();
        model.addAttribute("users",users);
        //插入成功返回，失败不返回
        return "manage";
    }

    //删除用户
    @RequestMapping("userDelete")
    @RequiresPermissions("sys:user:delete")
    @ResponseBody
    public Long userDelete(@RequestBody Long user_id){
        int flag=sysUserDao.delete(user_id);
        if(flag>0){

            return user_id;
        }
        return 0L;
    }

    //查找用户
    @RequestMapping("userSelect")
    @RequiresPermissions("sys:user:list,sys:user:info")
    @ResponseBody
    public SysUser userSelect(Long userId){
        return sysUserDao.selectById(userId);
    }

    //修改用户
    @RequestMapping("userUpdate")
    @RequiresPermissions("sys:user:update,sys:role:select")
    @ResponseBody
    public SysUser userUpdate(@RequestBody SysUser sysUser){
        Integer flag=sysUserDao.update(sysUser);
        if(flag>0){
            //添加成功之后 清除缓存
            DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            MyShiroRealm shiroRealm = (MyShiroRealm) securityManager.getRealms().iterator().next();
            //清除权限 相关的缓存
            shiroRealm.clearAllCache();
            return sysUser;
        }
        return null;
    }


}
