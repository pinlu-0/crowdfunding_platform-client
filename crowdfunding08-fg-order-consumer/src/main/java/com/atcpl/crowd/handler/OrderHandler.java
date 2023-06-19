package com.atcpl.crowd.handler;

import com.atcpl.crowd.api.MySQLRemoteService;
import com.atcpl.crowd.entity.vo.AddressVO;
import com.atcpl.crowd.entity.vo.MemberLoginVO;
import com.atcpl.crowd.entity.vo.OrderProjectVO;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.handler
 * @ClassName：OrderHandler
 * @Date：2023/4/20 15:19
 * @Version：1.0.0
 * @Description TODO(支付订单控制器)
 */
@Controller
public class OrderHandler {


    /**
     * 注入远程访问接口
     */
    @Autowired
    MySQLRemoteService mySQLRemoteService;

    /**
     * 处理点击订单详情页面的支持发送的请求
     *
     * @param projectId ： 项目id
     * @param returnId  ： 回报id
     * @return
     */
    @RequestMapping("/to/pay_step_1/{projectId}/{returnId}")
    public String toPayStep1(@PathVariable("projectId") Integer projectId,
                             @PathVariable("returnId") Integer returnId,
                             HttpSession session) {
        // 调用远程接口查询数据
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVoRemote(projectId, returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            // 获取查询的数据
            OrderProjectVO orderProjectVO = resultEntity.getData();
            session.setAttribute("orderProjectVO", orderProjectVO);
        }
        // 执行完去往的页面
        return "pay_step_1";
    }


    /**
     * 查询当前登录用户的收件地址信息
     *
     * @param returnCount
     * @param session
     * @return
     */
    @RequestMapping("/to/pay_step_2/{returnCount}")
    public String toPayStep2(@PathVariable("returnCount") Integer returnCount,
                             HttpSession session) {

        // 把接收到的回报数量合并session域中
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        // 防止redis中的session中保存的值未修改，在运行一遍
        session.setAttribute("orderProjectVO", orderProjectVO);
        // 获取当前已登录用户的id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute("member");
        Integer memberId = memberLoginVO.getId();

        // 跟据id查询当前登录用户的收件地址信息
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVoRemote(memberId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVoList", list);
        }

        return "pay_step_2";
    }


    /**
     * 添加一个新地址
     *
     * @param addressVO
     * @return
     */
    @RequestMapping("/add/new/address")
    public String addNewAddress(AddressVO addressVO, HttpSession session) {
        // 调用远程接口中的方法,保存要添加的新地址的信息
        ResultEntity<String> resultEntity = mySQLRemoteService.addNewAddressRemote(addressVO);
        // 从session中获取orderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        // 从orderProjectVO对象中获取returnCount
        Integer returnCount = orderProjectVO.getReturnCount();
        // 重定向到pay_step_2页面
        return "redirect:http://localhost:/order/to/pay_step_2/" + returnCount;
    }

}
