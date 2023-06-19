package com.atcpl.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd
 * @ClassName：OrderApp
 * @Date：2023/4/20 13:55
 * @Version：1.0.0
 * @Description TODO(order主启动类)
 */

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class,args);
    }
}
