package com.atcpl.crowd.handler;

import com.atcpl.crowd.api.MySQLRemoteService;
import com.atcpl.crowd.constant.CrowdConstant;
import com.atcpl.crowd.entity.vo.PortalTypeVO;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author cpl
 * @date 2022/12/30
 * @apiNote（首页控制器）
 */
@Controller
public class PortalHandler {

    /**
     * 远程服务接口
     */
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    /**
     * 查询首页数据
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String showPortalPage(Model model){

        // 1.调用MySQLRemoteService提供的方法查询首页要显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();

        // 2.获取查询结果
        String result = resultEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){
            // 3.获取查询结果数据
            List<PortalTypeVO> data = resultEntity.getData();

            // 4.存入模型
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, data);

        }

        return "portal";
    }

}
