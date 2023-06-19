package com.atcpl.crowd.handler;

import com.atcpl.crowd.api.MySQLRemoteService;
import com.atcpl.crowd.api.RedisRemoteService;
import com.atcpl.crowd.config.ShortMessageProperties;
import com.atcpl.crowd.constant.CrowdConstant;
import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.po.OrderProjectPO;
import com.atcpl.crowd.entity.vo.MemberLoginVO;
import com.atcpl.crowd.entity.vo.MemberVO;
import com.atcpl.crowd.entity.vo.MyCrowdInfo;
import com.atcpl.crowd.util.CrowdUtil;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author cpl
 * @date 2022/12/31
 * @apiNote
 */
@Controller
public class MemberController {

    @Autowired
    ShortMessageProperties shortMessageProperties;

    @Autowired
    RedisRemoteService redisRemoteService;

    @Autowired
    MySQLRemoteService mySqlRemoteService;

    /**
     * 退出系统
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("/fg/auth/exit/to/login/page.html")
    public String exitSystem(HttpSession httpSession) {
        // 让session失效
        httpSession.invalidate();
        return "redirect:http://localhost";
    }

    /**
     * 执行登录请求
     *
     * @param loginacct
     * @param userpswd
     * @return
     */
    @ResponseBody
    @RequestMapping("/fg/auth/do/member/login.json")
    public ResultEntity<String> login(@RequestParam("loginacct") String loginacct,
                                      @RequestParam("userpswd") String userpswd,
                                      HttpSession httpSession) {
        // 调用远程接口，根据loginacct查询MemberPO对象，并封装在ResultEntity中
        ResultEntity<MemberPO> resultEntity = mySqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if (resultEntity.getData() == null) {
            // 如果没有查到说明账号不存在
            return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_NOT_EXISTS);
        }
        // 从resultEntity获取所查对象
        MemberPO memberPO = resultEntity.getData();
        if (memberPO == null) {
            return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 获取数据库密码
        String DBUserPswd = memberPO.getUserpswd();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 将前端传输过来的密码与数据库密码进行比较
        boolean matches = bCryptPasswordEncoder.matches(userpswd, DBUserPswd);
        if (!matches) {
            // 密码不相同
            return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
        } else {
            // 密码相同则存进session域中
            // 创建MemberLoginVO
            MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail(), memberPO.getAuthstatus());
            httpSession.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
            return ResultEntity.successWithoutData();
        }

    }


    /**
     * 执行注册
     *
     * @param memberVO
     * @return
     */
    @ResponseBody
    @RequestMapping("/fg/auth/do/member/register.json")
    public ResultEntity<String> register(MemberVO memberVO) {
        // 获取前端用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 拼Redis中存储手机号的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        // 从Redis读取key对应的value
        ResultEntity<String> valueRemoteByKey = redisRemoteService.getRedisStringValueRemoteByKey(key);
        // 检查查询操作是否有效：1. redis远程方法调用结果是否为success；2. 读取到key的值value是否为null
        String result = valueRemoteByKey.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            // redis中的验证码
            String redisCode = valueRemoteByKey.getData();
            if (redisCode != null) {
                // 表单验证码
                String formCode = memberVO.getCode();
                // 比较表单验证码与redis中获取的验证码
                if (Objects.equals(redisCode, formCode)) {

                    // 执行密码加密
                    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                    // 获取前端表单用户输入的密码
                    String userpswd = memberVO.getUserpswd();
                    // 进行加密后的带盐值的密码
                    String encode = bCryptPasswordEncoder.encode(userpswd);
                    memberVO.setUserpswd(encode);
                    // 执行保存
                    MemberPO memberPO = new MemberPO();
                    // 赋值属性
                    BeanUtils.copyProperties(memberVO, memberPO);
                    ResultEntity<String> saveMemberResultEntity = mySqlRemoteService.saveMember(memberPO);
                    if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
                        return ResultEntity.failed(saveMemberResultEntity.getErrorMessage());
                    }
                    // 如果一致，删除redis中存储的这个手机号
                    redisRemoteService.removeRedisKeyRemote(key);
                } else {
                    return ResultEntity.failed(CrowdConstant.MESSAGE_CODE_INVALID);
                }
            } else {
                return ResultEntity.failed(CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            }
        } else {
            return ResultEntity.failed(valueRemoteByKey.getErrorMessage());
        }
        // 注册成功跳转到登录页
        return ResultEntity.successWithoutData();
    }

    /**
     * 发送短信获取验证码
     *
     * @param phoneNum
     * @return
     */
    @ResponseBody
    @RequestMapping("/fg/auth/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        // 发送验证码到 phoneNum
        ResultEntity<String> sendShortMessageResultEntity = CrowdUtil.sendShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(), phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin());
        // 判断发送结果
        if (ResultEntity.SUCCESS.equals(sendShortMessageResultEntity.getResult())) {
            // 拼接一个用于在Redis中存储数据的key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            // 获取随机验证码
            String code = sendShortMessageResultEntity.getData();

            // 调用远程接口存入Redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendShortMessageResultEntity;
        }
    }

    /**
     * 查询我的所有众筹项目
     * @param memberId
     * @return
     */
    @ResponseBody
    @RequestMapping("/get/my/crowd")
    public ResultEntity<List<MyCrowdInfo>> getMyCrowd(@RequestParam("memberId") Integer memberId) {
        List<MyCrowdInfo> data = new ArrayList<>();
        try {
            ResultEntity<List<MyCrowdInfo>> resultEntity = mySqlRemoteService.getMyCrowd(memberId);
            if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
                data = resultEntity.getData();
            }
            return ResultEntity.successWithData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
