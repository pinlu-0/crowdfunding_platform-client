package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.entity.vo
 * @ClassName：AddressVO
 * @Date：2023/4/20 22:02
 * @Version：1.0.0
 * @Description TODO(这里用一句话描述这个类的作用)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    private static final long serialVersionUID = -6389000045118020638L;
    /**
     * 地址id
     */
    private Integer id;

    /**
     * 收件人信命
     */
    private String receiveName;

    /**
     * 收件人手机号
     */
    private String phoneNum;

    /**
     * 收件人地址
     */
    private String address;

    /**
     * 用户id
     */
    private Integer memberId;
}
