package com.atcpl.crowd.service.impl;

import com.atcpl.crowd.entity.po.*;
import com.atcpl.crowd.entity.vo.AddressVO;
import com.atcpl.crowd.entity.vo.MyCrowdInfo;
import com.atcpl.crowd.entity.vo.OrderProjectVO;
import com.atcpl.crowd.entity.vo.OrderVO;
import com.atcpl.crowd.mapper.AddressPOMapper;
import com.atcpl.crowd.mapper.OrderPOMapper;
import com.atcpl.crowd.mapper.OrderProjectPOMapper;
import com.atcpl.crowd.mapper.ProjectPOMapper;
import com.atcpl.crowd.service.OrderService;
import com.atcpl.crowd.service.ProjectProvideService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.service.impl
 * @ClassName：OrderServiceImpl
 * @Date：2023/4/20 15:49
 * @Version：1.0.0
 * @Description TODO(这里用一句话描述这个类的作用)
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    OrderPOMapper orderPOMapper;

    @Autowired
    AddressPOMapper addressPOMapper;

    @Autowired
    ProjectProvideService projectProvideService;

    @Autowired
    ProjectPOMapper projectPOMapper;


    @Override
    public OrderProjectVO getOrderProjectVo(Integer projectId, Integer returnId) {

        // 根据projectId查询项目信息
        return orderProjectPOMapper.selectOrderProjectVo(returnId);
    }


    @Override
    public List<AddressVO> getAddressVoList(Integer memberId) {

        AddressPOExample example = new AddressPOExample();
        AddressPOExample.Criteria criteria = example.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);
        ArrayList<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addNewAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressPOMapper.insert(addressPO);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertOrder(OrderVO orderVO, Integer memberId) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);

        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);
        // 添加订单
        orderPOMapper.insert(orderPO);

        // 获取刚保存的订单的id
        Integer id = orderPO.getId();

        // 赋值给orderProjectPO
        orderProjectPO.setOrderId(id);
        orderProjectPO.setMemberid(memberId);
        Integer projectId = orderProjectPO.getProjectId();
        int i = orderProjectPOMapper.insert(orderProjectPO);
        // 如果支付成功了就同时修改 项目表中的 已筹集金额与支持人数
        Integer returnCount = orderProjectPO.getReturnCount();
        Integer supportPrice = orderProjectPO.getSupportPrice();

        if (i == 1) {
            ProjectPO projectPO = projectPOMapper.selectByPrimaryKey(projectId);
            projectPO.setSupporter(projectPO.getSupporter() + 1);
            projectPO.setSupportmoney(projectPO.getSupportmoney() + returnCount * supportPrice);
            projectPOMapper.updateByPrimaryKeySelective(projectPO);
        }
    }



    @Override
    public List<MyCrowdInfo> getMyCrowd(Integer memberId) {
        List<MyCrowdInfo> myCrowdInfos = orderProjectPOMapper.getMyCrowd(memberId);
        return myCrowdInfos;
    }


}
