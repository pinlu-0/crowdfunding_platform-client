package com.atcpl.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cpl
 * @date 2023/1/9
 * @apiNote
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 这里的请求路径只是project本模块内部定义的，是不经过Zuul访问地址的。故不需要加 /project。
        // 点击 发起众筹 来到同意协议页面
        registry.addViewController("/agree/protocol/page.html").setViewName("project-agree-protocol");
        // 点击 阅读并同意协议 按钮 到 发起项目页
        registry.addViewController("/launch/item/page.html").setViewName("project-launch");
        // 点击 下一步 来到回报设置页面
        registry.addViewController("/return/info/page.html").setViewName("project-retribution");
        // 点击 下一步 来到确认信息页面
        registry.addViewController("/create/confirm/page.html").setViewName("project-confirm");
        // 点击提交 来到完成页面
        registry.addViewController("/create/finished/page.html").setViewName("project-finished");
    }
}
