package com.atcpl.crowd.service;

import com.atcpl.crowd.entity.po.MemberPO;

/**
 * @author cpl
 * @date 2022/12/29
 * @apiNote
 */
public interface MemberProviderService {
    /**
     * 根据账户查询Member对象
     * @param loginacct
     * @return
     */
    MemberPO getMemberPOByLoginAcct(String loginacct);

    /**
     * 添加用户
     * @param memberPO
     */
    void saveMemberPO(MemberPO memberPO);



}
