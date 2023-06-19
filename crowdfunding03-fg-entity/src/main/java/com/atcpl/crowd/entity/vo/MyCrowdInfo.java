package com.atcpl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.entity.vo
 * @ClassName：MyCrowdInfo
 * @Date：2023/4/24 1:41
 * @Version：1.0.0
 * @Description TODO(我的众筹中的项目信息)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCrowdInfo {
    private String projectName;
    private String payOrderNum;
    private Long supportMoney;
    private Integer money;
    private Integer supportPrice;
    private String lastDay;
    private float percentage;
    private Integer returnCount;
    private Integer freight;
}
