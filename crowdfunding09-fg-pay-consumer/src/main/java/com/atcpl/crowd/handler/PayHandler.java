package com.atcpl.crowd.handler;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atcpl.crowd.api.MySQLRemoteService;
import com.atcpl.crowd.config.AlipayConfig;
import com.atcpl.crowd.entity.vo.MemberLoginVO;
import com.atcpl.crowd.entity.vo.OrderProjectVO;
import com.atcpl.crowd.entity.vo.OrderVO;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd
 * @ClassName：PayHandler
 * @Date：2023/4/21 16:34
 * @Version：1.0.0
 * @Description TODO(支付信息控制器)
 */

@Controller
public class PayHandler {


    @Autowired
    AlipayConfig AlipayConfig;

    @Autowired
    MySQLRemoteService mySQLRemoteService;

    /**
     * 生成订单
     *
     * @param orderVO
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("generate/payment/order")
    public String generateOrder(OrderVO orderVO, HttpSession session) throws AlipayApiException {

        // 从session中获取orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        // 将orderProjectVO与orderVO对象组合到一起
        orderVO.setOrderProjectVO(orderProjectVO);

        // 生成支付宝订单号并设置到orderVO对象中
        // 根据日期生成字符串
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 使用uuid生成用户id部分
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        // 组装订单号
        String orderNum = time + uuid;

        // 存入orderVO中
        orderVO.setOrderNum(orderNum);

        // 计算订单总金额并设置到orderVO对象中
        Double orderAmount = (double) (orderProjectVO.getSupportPrice() * orderProjectVO.getReturnCount() + orderProjectVO.getFreight());

        // 存入orderVO
        orderVO.setOrderAmount(orderAmount);

        // 把orderVO存入session域中
        session.setAttribute("orderVO", orderVO);

        return sendRequestToAliPay(orderNum, orderAmount, orderProjectVO.getProjectName(), orderProjectVO.getReturnContent());
    }

    /**
     * 为了调用支付宝接口专门封装的方法
     *
     * @param orderNum    // 商户订单号，商户网站订单系统中唯一订单号，必填
     * @param orderAmount // 付款金额，必填
     * @param subject     // 订单名称，必填 这里用项目名称
     * @param body        // 商品描述，可空 这里用回报的描述
     * @return 返回页面
     * @throws AlipayApiException
     */
    private String sendRequestToAliPay(String orderNum, Double orderAmount, String subject, String body) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.getGatewayUrl(), AlipayConfig.getAppId(), AlipayConfig.getMerchantPrivateKey(), "json", AlipayConfig.getCharset(), AlipayConfig.getAliPayPublicKey(), AlipayConfig.getSignType());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(AlipayConfig.getNotifyUrl());

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderNum + "\","
                + "\"total_amount\":\"" + orderAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
    }


    @RequestMapping("/return")
    public String returnUrlMethod(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException, AlipayApiException {
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.getAliPayPublicKey(), AlipayConfig.getCharset(), AlipayConfig.getSignType());

        if (signVerified) {
            // 商户订单号
            String orderNum = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String alipayOrderNum = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String orderAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            // 保存到数据库
            // 从session域中获取orderVO
            OrderVO orderVO = (OrderVO) session.getAttribute("orderVO");

            // 获取当前登录的用户id
            MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute("member");
            Integer memberId = memberLoginVO.getId();

            // 将支付宝交易号设置到OrderVO对象中
            orderVO.setAlipayOrderNum(alipayOrderNum);
            // 调用远程方法执行添加
            ResultEntity<String> resultEntity = mySQLRemoteService.saveOrderRemote(orderVO,memberId);

            // 返回的页面()
            //return "trade_no:" + orderNum + "<br/>out_trade_no:" + alipayOrderNum + "<br/>total_amount:" + alipayOrderNum;
            return "redirect:http://localhost/fg/auth/to/my/crowd/page.html";
        } else {
            return "验签失败";
        }
    }

    @RequestMapping("/notify")
    public void NotifyUrlMethod(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.getAliPayPublicKey(), AlipayConfig.getCharset(), AlipayConfig.getSignType());


        /* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        4、验证app_id是否为该商户本身。
        */
        //验证成功
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");


            //验证失败
        } else {

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }
    }

}
