package com.atcpl.crowd.service;

import com.atcpl.crowd.entity.po.CertPO;
import com.atcpl.crowd.entity.po.MemberCert;
import com.atcpl.crowd.entity.po.MemberPO;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.service
 * @ClassName：RealNameAuthentication
 * @Date：2023/4/14 12:41
 * @Version：1.0.0
 * @Description TODO(实名认证接口层)
 */
public interface RealNameAuthenticationService {

    /**
     * 根据会员id修改会员表的信息
     *
     * @param memberPO
     * @return
     */
    @RequestMapping("/update/member/info/by/member")
    Integer updateMemberInfo(MemberPO memberPO);

    /**
     * 根据账户类型id查询该账户类型对应的资质
     *
     * @param accountTypeId
     * @return
     */
    List<CertPO> getAllCertByAccountTypeId(Integer accountTypeId);


    /**
     * 往t_member_cert表中批量添加数据
     *
     * @param list
     * @return
     */
    Integer insertBatch(List<MemberCert> list);

    /**
     * 发送邮件
     *
     * @param email
     * @param username
     * @param memberId
     * @return
     */
    void sendEmail(String email, String username, Integer memberId);

    /**
     * 校验验证码
     * @param code
     * @param memberId
     * @return
     */
    boolean validCode(String code,Integer memberId);

    /**
     * 更改认证状态
     * @param memberPO
     * @return
     */
    Integer updateAuthStatus(MemberPO memberPO);

    /**
     * 查询认证状态
     * @param memberId
     * @return
     */
    MemberPO getAuthStatus(Integer memberId);
}
