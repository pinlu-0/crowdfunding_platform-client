package com.atcpl.crowd.service.impl;

import com.atcpl.crowd.entity.po.MemberTicket;
import com.atcpl.crowd.entity.po.MemberTicketExample;
import com.atcpl.crowd.mapper.MemberTicketMapper;
import com.atcpl.crowd.service.MemberTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.service.impl
 * @ClassName：MemberTicketServiceImpl
 * @Date：2023/4/17 20:29
 * @Version：1.0.0
 * @Description TODO(用户工单业务逻辑接口层实现层)
 */
@Service
public class MemberTicketServiceImpl implements MemberTicketService {

    @Autowired
    MemberTicketMapper memberTicketMapper;

    @Override
    public MemberTicket getAuthTicket(Integer memberId) {

        MemberTicketExample example = new MemberTicketExample();
        MemberTicketExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        criteria.andTickettypeEqualTo("realName");

        List<MemberTicket> list = memberTicketMapper.selectByExample(example);
        // 这个表中可能有很多当前用户的工单或者有很多种类，
        int size = list.size();
        MemberTicket ticket = list.get(size - 1);
        return ticket;
    }
}
