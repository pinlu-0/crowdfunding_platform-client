package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.entity.vo
 * @ClassName：OrderProjcetVO
 * @Date：2023/4/20 15:17
 * @Version：1.0.0
 * @Description TODO(支付订单信息使用的实体类)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProjectVO implements Serializable {

    private static final long serialVersionUID = -4794342084491869206L;

    private Integer id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 支持者名称
     */
    private String launchName;

    /**
     * 回报描述内容
     */
    private String returnContent;

    /**
     * 回报数量
     */
    private Integer returnCount;

    /**
     * 支持单价
     */
    private Integer supportPrice;

    /**
     * 运费
     * 值为0代表包邮，免邮费
     * 值不为0代表不包邮，不免邮费
     */
    private Integer freight;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 是否设置单笔限购
     */
    private Integer signalPurchase;

    /**
     * 具体限购数量
     */
    private Integer purchase;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 会员id
     */
    private Integer memberid;
}
