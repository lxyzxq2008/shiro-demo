package com.sunroom.shiro.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {

    @GetMapping("/login")
    public String onLogin() {

        return "login/login";
    }

    @PostMapping("/login")
    public String onLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam(value = "rememberMe", required = false, defaultValue = "false") Boolean rememberMe) {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                UsernamePasswordToken token  = new UsernamePasswordToken();
                token.setUsername(username);
                token.setPassword(password.toCharArray());
                token.setRememberMe(rememberMe);
                subject.login(token);
                return "redirect:/index.html";
            }
        } catch (UnknownAccountException ex) {
            ex.printStackTrace();
        }catch (IncorrectCredentialsException ex) {
            ex.printStackTrace();
        }catch (LockedAccountException ex) {
            ex.printStackTrace();
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
        }
        return "redirect:/login/login.html";
    }

    @GetMapping("/test01")
    public String onTest() {

        return "test01";
    }

    @GetMapping("/logout")
    public String onLogout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/login/login.html";
    }
}
