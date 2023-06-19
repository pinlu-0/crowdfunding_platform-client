package com.atcpl.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd
 * @ClassName：PayApp
 * @Date：2023/4/21 17:26
 * @Version：1.0.0
 * @Description TODO(这里用一句话描述这个类的作用)
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PayApp {
    public static void main(String[] args) {
        SpringApplication.run(PayApp.class,args);
    }
}
