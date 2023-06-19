package com.atcpl.crowd.service;

import com.atcpl.crowd.entity.po.OrderProjectPO;
import com.atcpl.crowd.entity.vo.AddressVO;
import com.atcpl.crowd.entity.vo.MyCrowdInfo;
import com.atcpl.crowd.entity.vo.OrderProjectVO;
import com.atcpl.crowd.entity.vo.OrderVO;

import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.service
 * @ClassName：OrderService
 * @Date：2023/4/20 15:49
 * @Version：1.0.0
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public interface OrderService {

    /**
     * 查询项目订单信息
     * @param projectId
     * @param returnId
     * @return
     */
    OrderProjectVO getOrderProjectVo(Integer projectId, Integer returnId);

    /**
     * 根据用户Id查询当前登录用户的收件地址信息
     * @param memberId
     * @return
     */
    List<AddressVO> getAddressVoList(Integer memberId);

    /**
     * 添加一个新地址
     * @param addressVO
     */
    void addNewAddress(AddressVO addressVO);

    /**
     * 添加支付订单
     * @param orderVO
     * @param memberId
     */
    void insertOrder(OrderVO orderVO,Integer memberId);

    /**
     * 查询我的众筹项目
     * @param memberId
     * @return
     */
    List<MyCrowdInfo> getMyCrowd(Integer memberId);
}
