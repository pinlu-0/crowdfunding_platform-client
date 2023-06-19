package com.atcpl.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



/**
 * 启用Feign客户端
 * @author cpl
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ProjectApp {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApp.class,args);
    }
}
