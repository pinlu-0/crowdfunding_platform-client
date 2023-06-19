package com.atcpl.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPO {

    private Integer id;

    private String projectName;

    private String projectDescription;

    private Integer money;

    /**
     * 项目筹集总天数
     */
    private Integer day;

    private Integer status;

    /**
     * 项目截止日期
     */
    private LocalDate deploydate;

    /**
     * 已筹集到的资金
     */
    private Long supportmoney;

    /**
     * 支持者数量
     */
    private Integer supporter;

    private Integer completion;

    /**
     * 用户ID
     */
    private Integer memberid;

    /**
     * 项目开始筹集时间
     */
    private String createdate;

    /**
     * 关注人数
     */
    private Integer follower;

    private String headerPicturePath;

}