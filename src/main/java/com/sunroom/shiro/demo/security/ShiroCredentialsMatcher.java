package com.sunroom.shiro.demo.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

public class ShiroCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        System.out.println("doCredentialsMatch:-->");
        System.out.println("doCredentialsMatch:-->" + authenticationToken);
        System.out.println("doCredentialsMatch:-->" + authenticationInfo);
        return true;
    }
}
