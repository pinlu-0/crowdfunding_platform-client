package com.atcpl.crowd.service;

import com.atcpl.crowd.entity.vo.DetailProjectVO;
import com.atcpl.crowd.entity.vo.PortalProjectVO;
import com.atcpl.crowd.entity.vo.PortalTypeVO;
import com.atcpl.crowd.entity.vo.ProjectVO;

import java.util.List;

/**
 * @author cpl
 * @date 2023/1/9
 * @apiNote TODO(项目模块的接口)
 */
public interface ProjectProvideService {

    /**
     * 发布项目 接口层
     * @param projectVO
     * @param id
     */
    void saveProject(ProjectVO projectVO, Integer id);

    /**
     * 主页展示项目 接口层
     * @return
     */
    List<PortalTypeVO> getPortalTypeVO();

    /**
     * 根据项目id查询项目详情信息
     * @param projectId
     * @return
     */
    DetailProjectVO getDetailProjectVO(Integer projectId);


}
