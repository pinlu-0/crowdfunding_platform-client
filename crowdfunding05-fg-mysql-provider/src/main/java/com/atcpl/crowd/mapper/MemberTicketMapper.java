package com.atcpl.crowd.mapper;


import com.atcpl.crowd.entity.po.MemberTicket;
import com.atcpl.crowd.entity.po.MemberTicketExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTicketMapper {
    int countByExample(MemberTicketExample example);

    int deleteByExample(MemberTicketExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberTicket record);

    int insertSelective(MemberTicket record);

    List<MemberTicket> selectByExample(MemberTicketExample example);

    MemberTicket selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberTicket record, @Param("example") MemberTicketExample example);

    int updateByExample(@Param("record") MemberTicket record, @Param("example") MemberTicketExample example);

    int updateByPrimaryKeySelective(MemberTicket record);

    int updateByPrimaryKey(MemberTicket record);
}