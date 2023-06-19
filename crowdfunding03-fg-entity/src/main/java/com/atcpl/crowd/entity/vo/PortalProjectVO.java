package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author cpl
 * @date 2023/1/27
 * @apiNote
 * 主页项目显示大概信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目头图
     */
    private String headerPicturePath;

    /**
     * 筹资资金
     */
    private Integer money;

    /**
     * 部署日期
     */
    private Date deployDate;

    /**
     * 筹集进度
     */
    private Integer percentage;

    /**
     * 支持者
     */
    private Integer supporter;

}
