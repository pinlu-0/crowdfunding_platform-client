package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.entity.vo
 * @ClassName：OrderVO
 * @Date：2023/4/21 16:27
 * @Version：1.0.0
 * @Description TODO(订单属性实体类)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {
    private static final long serialVersionUID = -502160432808968171L;
    /**
     * 主键
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 支付宝流水单号
     */
    private String alipayOrderNum;

    /**
     * 订单金额
     */
    private Double orderAmount;

    /**
     * 是否开发票
     */
    private Integer invoice;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 订单备注
     */
    private String orderRemark;

    /**
     * 地址信息表的主键
     */
    private Integer addressId;

    /**
     * 项目信息实体类对象
     */
    private OrderProjectVO orderProjectVO;
}
