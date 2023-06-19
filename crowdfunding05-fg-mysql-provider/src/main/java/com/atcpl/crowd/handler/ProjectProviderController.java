package com.atcpl.crowd.handler;

import com.atcpl.crowd.entity.vo.DetailProjectVO;
import com.atcpl.crowd.entity.vo.PortalTypeVO;
import com.atcpl.crowd.entity.vo.ProjectVO;
import com.atcpl.crowd.service.ProjectProvideService;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author cpl
 * @date 2023/1/9
 * @apiNote
 */
@RestController
public class ProjectProviderController {

    @Autowired
    private ProjectProvideService projectProvideService;

    /**
     * 发布项目 控制层
     * @param projectVO
     * @param id
     * @return
     */
    @RequestMapping("/save/projectVO/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("id") Integer id) {
        try {
            // 调用同模块中的Service执行saveProject方法
            projectProvideService.saveProject(projectVO, id);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 展示前端首页项目 控制层
     * @return
     */
    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote(){

        try {
            List<PortalTypeVO> portalTypeVOList = projectProvideService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据项目id查询项目详情信息
     * @param projectId
     * @return
     */
    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId){
        try {
            DetailProjectVO detailProjectVO = projectProvideService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
