package com.atcpl.crowd.service;

import com.atcpl.crowd.entity.po.MemberTicket;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.service
 * @ClassName：MemberTicketService
 * @Date：2023/4/17 20:26
 * @Version：1.0.0
 * @Description TODO(用户工单业务逻辑接口层)
 */
public interface MemberTicketService {

    /**
     * 根据用户id查询工单
     * @param memberId
     * @return
     */
    MemberTicket getAuthTicket(Integer memberId);
}
