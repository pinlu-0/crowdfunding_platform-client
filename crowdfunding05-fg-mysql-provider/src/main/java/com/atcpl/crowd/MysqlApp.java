package com.atcpl.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author cpl
 * @date 2022/12/29
 * @apiNote
 */
@MapperScan("com.atcpl.crowd.mapper")
@SpringBootApplication
public class MysqlApp {
    public static void main(String[] args) {
        SpringApplication.run(MysqlApp.class,args);
    }
}
