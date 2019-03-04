package com.sunroom.shiro.demo.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

public class DemoRealm extends AuthenticatingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("realm-->" + token);
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        // 从数据库中检索出用户信息，进行如下处理：
        if ("unknowuser".equals(username)) {
            throw new UnknownAccountException("用户不存在");
        }

        if ("lockeduser".equals(username)) {
            throw new LockedAccountException("用户被锁定");
        }

        Object principal = username;
        // 密码是数据库检索出来的
//        Object credentials = "123";
        Object credentials = "c41d7c66e1b8404545aa3a0ece2006ac";
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, getName());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, getName());
        return info;
    }

//    public static void main(String[] args) {
//        String hashAlgorithmName = "MD5";
//        Object credentials = "123";
//        Object salt = ByteSource.Util.bytes("admin");;
//        int hashIterations = 1024;
//
//        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
//        System.out.println(result);
//    }
}
