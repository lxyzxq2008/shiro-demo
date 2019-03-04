package com.sunroom.shiro.demo.config;

import com.sunroom.shiro.demo.realms.Demo01Realm;
import com.sunroom.shiro.demo.realms.DemoRealm;
import com.sunroom.shiro.demo.security.CacheSessionDao;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.*;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ShiroConfiguration {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login");

        // 拦截配置
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/*.html", "authc");
        filterChainDefinitionMap.put("/**", "authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(
            Authenticator authenticator,
            SessionManager sessionManager,
            CacheManager cacheManager,
            RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(realm);
        securityManager.setAuthenticator(authenticator);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    @Bean
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        SimpleCookie cookie = new SimpleCookie();
        cookie.setMaxAge(10);
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }

    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
        defaultSessionManager.setGlobalSessionTimeout(1800000);
        defaultSessionManager.setDeleteInvalidSessions(true);
        defaultSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultSessionManager.setSessionDAO(sessionDAO);
        return defaultSessionManager;
    }

    @Bean
    public SessionDAO sessionDAO() {
        CacheSessionDao sessionDAO = new CacheSessionDao();
        return sessionDAO;
    }

    @Bean
    public Authenticator authenticator(
            AuthenticatingRealm realm,
            AuthenticatingRealm realm01) {
        List<Realm> realms = new ArrayList<>();
        realms.add(realm);
        realms.add(realm01);

        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // AllSuccessfulStrategy: 所有都成功
        // FirstSuccessfulStrategy: 一个成功，只返回第一个成功的信息
        // AtLeastOneSuccessfulStrategy: 一个成功，返回所有成功的信息
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        authenticator.setRealms(realms);
        return authenticator;
    }

    @Bean
    public AuthenticatingRealm realm() {
        DemoRealm demoRealm = new DemoRealm();
        demoRealm.setCredentialsMatcher(credentialsMatcher());
        return demoRealm;
    }
    @Bean
    public AuthenticatingRealm realm01() {
        Demo01Realm demo01Realm = new Demo01Realm();
        demo01Realm.setCredentialsMatcher(credentialsMatcher());
        return demo01Realm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
//        CredentialsMatcher credentialsMatcher = new ShiroCredentialsMatcher();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);

        return credentialsMatcher;
    }
}
