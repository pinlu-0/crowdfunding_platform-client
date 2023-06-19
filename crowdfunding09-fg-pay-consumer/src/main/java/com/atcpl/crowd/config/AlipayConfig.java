package com.atcpl.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.config
 * @ClassName：AlipayConfig
 * @Date：2023/4/21 17:16
 * @Version：1.0.0
 * @Description TODO(阿里支付属性实体类)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "ali.pay")
public class AlipayConfig {
    private String appId;

    /**
     * 生成公钥是对应的私钥
     */
    private String merchantPrivateKey;

    /**
     * 阿里的公钥
     */
    private String aliPayPublicKey;

    /**
     * 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    private String notifyUrl;

    /**
     * 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
     */
    private String returnUrl;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 编码
     */
    private String charset;

    /**
     * 支付宝网关
     */
    private String gatewayUrl;
}
