package com.atcpl.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cpl
 * @date 2022/12/31
 * @apiNote
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 发送请求 到 注册页面
        registry.addViewController("/fg/auth/to/register/page.html").setViewName("member-register");

        // 注册成功，发送请求 到 登录页面
        registry.addViewController("/fg/auth/to/login/page.html").setViewName("member-login");

        //  到个人中心页
        //registry.addViewController("/fg/auth/to/center/page.html").setViewName("member-center");

        // 点击我的众筹 到  我的众筹页面
        registry.addViewController("/fg/auth/to/my/crowd/page.html").setViewName("member-crowd");

        // 点击未实名认证 到 账户类型选择页面
        registry.addViewController("/fg/auth/to/select/account.html").setViewName("select-account");

    }



}
