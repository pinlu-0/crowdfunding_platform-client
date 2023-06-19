package com.atcpl.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cpl
 * @date 2022/12/31
 * @apiNote
 * 调用短信接口的属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {
    /**
     * 短信接口调用地址
     */
    private String host;

    /**
     * 路径
     */
    private String path;

    /**
     * 请求方式为POST
     */
    private String method;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 阿里的appCode
     */
    private String appCode;

    /**
     * 短信前缀
     */
    private String sign;

    /**
     * 短信模板
     */
    private String skin;
}
