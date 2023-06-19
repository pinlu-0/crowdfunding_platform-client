package com.atcpl.crowd.mapper;

import com.atcpl.crowd.entity.po.OrderProjectPO;
import com.atcpl.crowd.entity.po.OrderProjectPOExample;
import com.atcpl.crowd.entity.vo.MyCrowdInfo;
import com.atcpl.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderProjectPOMapper {
    int countByExample(OrderProjectPOExample example);

    int deleteByExample(OrderProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProjectPO record);

    int insertSelective(OrderProjectPO record);

    List<OrderProjectPO> selectByExample(OrderProjectPOExample example);

    OrderProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByExample(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByPrimaryKeySelective(OrderProjectPO record);

    int updateByPrimaryKey(OrderProjectPO record);

    /**
     * 根据回报id查询项目订单信息
     * @param returnId
     * @return
     */
    OrderProjectVO selectOrderProjectVo(Integer returnId);



    /**
     * 自定义SQL语句查询我众筹的项目信息
     * @param memberId
     * @return
     */
    List<MyCrowdInfo> getMyCrowd(Integer memberId);
}