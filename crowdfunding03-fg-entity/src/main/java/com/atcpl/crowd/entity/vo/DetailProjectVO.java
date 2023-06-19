package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author cpl
 * @date 2023/1/31
 * @apiNote TODO（项目详情信息实体类）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO {
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String projectDesc;

    /**
     * 当前项目关注数量
     */
    private Integer followerCount;

    /**
     * 项目筹集状态 （已开始筹集或为开始或已完成）
     */
    private Integer status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 项目筹集目标资金
     */
    private Integer money;

    /**
     * 项目已筹集资金
     */
    private Integer supportMoney;

    /**
     * 项目筹集进度
     */
    private Integer percentage;

    /**
     * 项目筹集所剩日期时间
     */
    private String deployDate;

    /**
     * 项目筹集的总天数
     */
    private Integer day;

    /**
     * 所剩天数
     */
    private Integer lastDay;

    /**
     * 支持者数量
     */
    private Integer supporterCount;

    /**
     * 头图路径
     */
    private String headerPicturePath;

    /**
     * 详情图片路径
     */
    private List<String> detailPicturePathList;

    /**
     * 回报详情对象集合
     */
    private List<DetailReturnVO> detailReturnVOList;

}
