package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cpl
 * @date 2023/1/31
 * @apiNote TODO（回报信息实体类）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {
    /**
     * 回报信息id
     */
    private Integer returnId;

    /**
     * 当前档位需要支持的金额
     */
    private Integer supportMoney;

    /**
     * 单笔限购，取0时有限额 取值为1无限额
     */
    private Integer singlePurchase;

    /**
     * 具体限额数量
     */
    private Integer purchase;

    /**
     * 当该档位前支持者数量
     */
    private Integer supporterCount;

    /**
     * 运费 取0为包邮
     */
    private Integer freight;

    /**
     * 筹集成功后多少天发货
     */
    private Integer returnDate;

    /**
     * 回报内容简介
     */
    private String content;
}
