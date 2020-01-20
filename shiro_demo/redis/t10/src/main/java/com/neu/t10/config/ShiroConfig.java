package com.neu.t10.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.neu.t10.Filter.KickoutSessionControlFilter;
import com.neu.t10.Realm.MyShiroRealm;
import com.neu.t10.Realm.ShiroSessionListener;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     *  解决@RequiresPermissions注解不生效的问题
     * @return
     */
//    @Bean
//    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
//        return new DefaultAdvisorAutoProxyCreator();
//    }

    /**
     * 配置拦截的url和无权限跳转的url
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        /**
         * 限制不同浏览器的登陆
         */
        //自定义拦截器限制并发人数,
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //限制同一帐号同时在线的个数
        filtersMap.put("kickout", kickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

        // 设置默认登录的url，身份认证失败会访问该url
        shiroFilterFactoryBean.setLoginUrl("/toLogin");

        /**
         * 默认配置
         */
        // 配置访问权限 必须是LinkedHashMap，因为它必须保证有序
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 --> : 这是一个坑，一不小心代码就不好使了
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //配置不登录可以访问的资源，anon 表示资源都可以匿名访问
        //配置验证码的地址
        filterChainDefinitionMap.put("/Captcha.jpg","anon");
        //配置解锁用户的地址
        filterChainDefinitionMap.put("/unlockAccount", "anon");
        //配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/login", "kickout,anon");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问; user:认证通过或者配置了记住我-->
        //kickout表示 访问/**下的资源 首先要通过 kickout 后面的filter，然后再通过user后面对应的filter才可以访问。
        filterChainDefinitionMap.put("/**", "kickout,user");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        // 登录成功后要跳转的链接,这里/userList是后台的接口名，非页面
        shiroFilterFactoryBean.setSuccessUrl("/userList");
        //未授权会跳转的界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

//    /**
//     * 凭证匹配器
//     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
//     * ）
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        myShiroRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        myShiroRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        myShiroRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        myShiroRealm.setAuthorizationCacheName("authorizationCache");
        //配置自定义密码比较器
        myShiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return myShiroRealm;
    }

    /*
    ,这里偶尔securityManager会报错,
    可能原因是我的myShiroRealm和其中new的一个myShiroRealm冲突,
    可以把上面new的myShiroRealm改一下名字
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //设置自定义realm
        securityManager.setRealm(myShiroRealm());
        //配置记住我
        securityManager.setRememberMeManager(rememberMeManager());
        //配置 redis缓存管理器 参考博客：
        securityManager.setCacheManager(cacheManager());
        //配置自定义session管理，使用ehcache 或redis
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    /**
     * 必须（thymeleaf页面使用shiro标签控制按钮是否显示）
     * 未引入thymeleaf包，Caused by: java.lang.ClassNotFoundException: org.thymeleaf.dialect.AbstractProcessorDialect
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    /**
     * 如果没有导入AOP的包，将不会生效
     *  开启shiro aop注解支持.
     *  可以在controller中的方法前加上注解
     *  如 @RequiresPermissions("userInfo:add")
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 解决： 无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized") 无效
     * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
     * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
     * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
     * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
     * @return
     */
    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver
    createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException","403");
        r.setExceptionMappings(mappings);  // None by default
        r.setDefaultErrorView("error");    // No default
        r.setExceptionAttribute("ex");     // Default is "exception"
        //r.setWarnLogCategory("example.MvcLogger");     // No default
        return r;
    }

    /**
     * 配置记住我
     */

    /**
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能,rememberMe管理器
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    /**
     * FormAuthenticationFilter 过滤器 过滤记住我
     * @return
     */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }


    /**
     * shiro整合ehcache缓存
     * shiro缓存管理器;
     * 需要添加到securityManager中
     * @return
     */
//    @Bean
//    public EhCacheManager ehCacheManager(){
//        EhCacheManager cacheManager = new EhCacheManager();
//        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
//        return cacheManager;
//    }

    /**
     * shiro整合redis缓存
     * shiro缓存管理器;
     * 需要添加到securityManager中
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("username");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }





    /**
     * 让某个实例的某个方法的返回值注入为Bean的实例
     * Spring静态注入
     * @return
     */
    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(){
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }

    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
//        redisManager.setHost("127.0.0.1");
//        redisManager.setPort(6379);
        return redisManager;
    }





    /**
     *  session管理
     */

    /**
     * 配置session监听
     * @return
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }

    /**
     * 配置会话ID生成器
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO=new RedisSessionDAO();
        //使用redisManager
        redisSessionDAO.setRedisManager(redisManager());
        //session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDAO.setExpire(12000);
        return redisSessionDAO;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置会话管理器，设定会话超时及保存
     * @return
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(cacheManager());

        //下面的配置是为了解决关闭浏览器时，不能够进行session的清空处理

        //全局会话超时时间（单位毫秒），默认30分钟 (1800000) 测试可以设置为10秒钟（也就是10000）
        sessionManager.setGlobalSessionTimeout(1800000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时（3600000）
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //测试可以设置为 5秒（也就是5000）
        sessionManager.setSessionValidationInterval(5000);

        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;

    }


    /**
     * 并发登录控制
     * @return
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionControlFilter;
    }

    /**
     * 配置密码比较器
     * @return
     */
    @Bean("credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher(){
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        retryLimitHashedCredentialsMatcher.setRedisManager(redisManager());
        //如果密码加密,可以打开下面配置
        //加密算法的名称
        //retryLimitHashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //配置加密的次数
        //retryLimitHashedCredentialsMatcher.setHashIterations(1024);
        //是否存储为16进制
        //retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);

        return retryLimitHashedCredentialsMatcher;
    }





}