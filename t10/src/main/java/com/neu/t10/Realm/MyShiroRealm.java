package com.neu.t10.Realm;


import com.neu.t10.dao.SysMenuDao;
import com.neu.t10.dao.SysRoleDao;
import com.neu.t10.dao.SysUserDao;
import com.neu.t10.entity.SysMenu;
import com.neu.t10.entity.SysRole;
import com.neu.t10.entity.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
//redis
//import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleDao sysRoleDao;


    /**
     * 权限的分配
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser userInfo  = (SysUser) principals.getPrimaryPrincipal();
        List<SysRole> userRoles=sysUserDao.selectUserRole(userInfo.getUserId());
        if(userRoles==null){
            System.out.println("空");
        }
        for(SysRole role:userRoles){
            authorizationInfo.addRole(role.getRoleName());
            List<SysMenu> rolePermissions=sysRoleDao.selectRoleMenu(role.getRoleId());
            for(SysMenu p:rolePermissions){
                if(p!=null){
                    if(p.getPerms()!=null) {
                        authorizationInfo.addStringPermission(p.getPerms());
                    }
                }

            }
        }
        System.out.println("权限分配完成");
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();

        System.out.println(token.getPrincipal());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUser userInfo = sysUserDao.selectByName(username);
        System.out.println("----->>userInfo="+userInfo);
        if(userInfo == null){
            return null;
        }

        // 这样通过配置中的 HashedCredentialsMatcher 进行自动校验
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

}