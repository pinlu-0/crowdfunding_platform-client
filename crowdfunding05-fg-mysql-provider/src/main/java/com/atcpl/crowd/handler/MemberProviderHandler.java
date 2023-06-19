package com.atcpl.crowd.handler;

import com.atcpl.crowd.constant.CrowdConstant;
import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.po.OrderProjectPO;
import com.atcpl.crowd.entity.vo.MyCrowdInfo;
import com.atcpl.crowd.mapper.OrderProjectPOMapper;
import com.atcpl.crowd.service.MemberProviderService;
import com.atcpl.crowd.service.OrderService;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cpl
 * @date 2022/12/29
 * @apiNote
 */
@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberProviderService memberProviderService;

    @Autowired
    OrderService orderService;


    /**
     * 注册会员
     *
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {// 远程调用，这里需要使用@RequestBody注解
        try {
            memberProviderService.saveMemberPO(memberPO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }


    /**
     * 通过loginacct查询MemberPO对象
     *
     * @param loginacct
     * @return
     */
    @RequestMapping("/get/member/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct) {
        try {
            MemberPO memberPO = memberProviderService.getMemberPOByLoginAcct(loginacct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            ResultEntity.failed(e.getMessage());
        }
        return null;
    }


    /**
     * 查询我众筹的项目
     *
     * @param memberId
     * @return
     */
    @RequestMapping("/get/my/crowd")
    public ResultEntity<List<MyCrowdInfo>> getMyCrowd(@RequestParam("memberId") Integer memberId) {
        List<MyCrowdInfo> myCrowdInfos = orderService.getMyCrowd(memberId);
        return ResultEntity.successWithData(myCrowdInfos);
    }

}
