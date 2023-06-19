package com.atcpl.crowd.handler;

import com.atcpl.crowd.entity.po.CertPO;
import com.atcpl.crowd.entity.po.MemberCert;
import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.vo.MemberLoginVO;
import com.atcpl.crowd.service.RealNameAuthenticationService;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.handler
 * @ClassName：MemberProviderController
 * @Date：2023/4/14 13:13
 * @Version：1.0.0
 * @Description TODO(这里用一句话描述这个类的作用)
 */
@RestController
public class RealNameAuthenticationProviderController {


    @Autowired
    RealNameAuthenticationService realNameAuthenticationService;

    /**
     * 实名认证，会员表中有些字段值在注册的时候为填写，所以通过修改，来将这些字段信息添加到会员表中
     *
     * @param memberPO
     * @return
     */
    @RequestMapping("/update/member/info/by/member")
    ResultEntity<Integer> updateMemberInfoRemote(@RequestBody MemberPO memberPO) {
        Integer i = realNameAuthenticationService.updateMemberInfo(memberPO);
        return ResultEntity.successWithData(i);
    }


    /**
     * 根据账户类型的id查询该账户类型对应的所有的资质
     *
     * @param accountTypeId
     * @return
     */
    @RequestMapping("/getAllCertsByAcctId")
    ResultEntity<List<CertPO>> getAllCertByAccountTypeId(@RequestParam("accountTypeId") Integer accountTypeId) {
        List<CertPO> list = realNameAuthenticationService.getAllCertByAccountTypeId(accountTypeId);
        return ResultEntity.successWithData(list);
    }


    /**
     * 资质文件上传
     *
     * @param list
     * @return
     */
    @RequestMapping("/upload/cert/file/remote")
    ResultEntity<Integer> upload(@RequestBody List<MemberCert> list) {
        Integer i = realNameAuthenticationService.insertBatch(list);
        return ResultEntity.successWithData(i);
    }


    /**
     * 发送邮件
     *
     * @param member
     * @return
     */
    @RequestMapping("/send/email/remote")
    ResultEntity<Integer> sendEmail(@RequestBody MemberLoginVO member) {
        // 从实体类中获取值
        Integer memberId = member.getId();
        String email = member.getEmail();
        String username = member.getUsername();
        // 调用方法
        realNameAuthenticationService.sendEmail(email, username, memberId);

        return ResultEntity.successWithData(1);
    }

    /**
     * 校验验证码
     *
     * @param code
     * @return
     */
    @RequestMapping("/valid/code")
    ResultEntity<Boolean> validCode(@RequestParam("code") String code, @RequestParam("memberId") Integer memberId) {

        // 领取流程任务，校验验证码
        boolean flag = realNameAuthenticationService.validCode(code, memberId);


        return ResultEntity.successWithData(flag);
    }


    /**
     * 更新认证状态
     * @param memberPO
     * @return
     */
    @RequestMapping("/update/auth/status")
    ResultEntity<Integer> updateAuthStatus(@RequestBody MemberPO memberPO){

        Integer count = realNameAuthenticationService.updateAuthStatus(memberPO);

        return ResultEntity.successWithData(count);
    }

    /**
     * 查询审核状态
     * @param memberId
     * @return
     */
    @RequestMapping("/get/auth/status")
    ResultEntity<MemberPO> getAuthStatus(@RequestParam("memberId") Integer memberId){
        MemberPO memberPO = realNameAuthenticationService.getAuthStatus(memberId);
        return ResultEntity.successWithData(memberPO);
    }

}
