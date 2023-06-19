package com.atcpl.crowd.handler;

import com.atcpl.crowd.entity.vo.AddressVO;
import com.atcpl.crowd.entity.vo.OrderProjectVO;
import com.atcpl.crowd.entity.vo.OrderVO;
import com.atcpl.crowd.service.OrderService;
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
 * @ClassName：OrderProviderHandler
 * @Date：2023/4/20 15:48
 * @Version：1.0.0
 * @Description TODO(Order模块的远程服务接口)
 */
@RestController
public class OrderProviderHandler {
    @Autowired
    OrderService orderService;

    /**
     * 查询支付订单的信息
     *
     * @param projectId
     * @param returnId
     * @return
     */
    @RequestMapping("/get/order/project/vo/Remote")
    ResultEntity<OrderProjectVO> getOrderProjectVoRemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId) {

        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVo(projectId, returnId);
            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }



    /**
     * 根据用户Id查询当前登录用户的收件地址信息
     * @param memberId
     * @return
     */
    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVoRemote(@RequestParam("memberId") Integer memberId){

        try {
            List<AddressVO> addressVos = orderService.getAddressVoList(memberId);
            return ResultEntity.successWithData(addressVos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }

    /**
     * 添加一个新地址
     * @param addressVO
     * @return
     */
    @RequestMapping("add/new/address/remote")
    ResultEntity<String> addNewAddressRemote(@RequestBody AddressVO addressVO){
        try {
            orderService.addNewAddress(addressVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 保存支付订单
     * @param orderVO
     * @param memberId
     * @return
     */
    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO,@RequestParam("memberId") Integer memberId){

        try {
            orderService.insertOrder(orderVO,memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.successWithData(e.getMessage());
        }
    }

}
