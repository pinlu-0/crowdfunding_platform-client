package com.atcpl.crowd.api;

import com.atcpl.crowd.entity.po.CertPO;
import com.atcpl.crowd.entity.po.MemberCert;
import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.po.OrderProjectPO;
import com.atcpl.crowd.entity.vo.*;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * MySQL模块远程接口
 * @author cpl
 * @date 2022/12/29
 * @apiNote
 */
@FeignClient("atcpl-crowd-mysql")
@Repository
public interface MySQLRemoteService {

    @RequestMapping("/get/member/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    /**
     * 注册会员远程接口
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/projectVO/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("id") Integer id);

    @RequestMapping("/get/portal/type/project/data/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    /**
     * 根据项目id查询项目详情信息
     * @param projectId
     * @return
     */
    @RequestMapping("/get/project/detail/remote/{projectId}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);






    /**
     * 实名认证，会员表中有些字段值在注册的时候为填写，所以通过修改，来将这些字段信息添加到会员表中
     * @param memberPO
     * @return
     */
    @RequestMapping("/update/member/info/by/member")
    ResultEntity<Integer> updateMemberInfoRemote(@RequestBody MemberPO memberPO);

    /**
     * 根据账户类型的id查询该账户类型对应的所有的资质
     * @param accountTypeId
     * @return
     */
    @RequestMapping("/getAllCertsByAcctId")
    ResultEntity<List<CertPO>>getAllCertByAccountTypeId(@RequestParam("accountTypeId") Integer accountTypeId);


    ///**
    // * 跨域资质文件上传
    // * @param file  是个数组，代表多文件上传
    // * @param certId
    // * @return
    // */
    //@RequestMapping("/upload/cert/file/remote")
    //ResultEntity<Object> upload(@RequestParam("file") MultipartFile[] file,@RequestParam("certid") Integer[] certId);

    /**
     * 资质文件上传
     * @param list
     * @return
     */
    @RequestMapping("/upload/cert/file/remote")
    ResultEntity<Integer> upload(@RequestBody List<MemberCert> list) ;

    /**
     * 发送邮件
     * @param member
     * @return
     */
    @RequestMapping("/send/email/remote")
    ResultEntity<Integer> sendEmail(@RequestBody MemberLoginVO member);

    /**
     * 校验验证码
     * @param code
     * @param memberId
     * @return
     */
    @RequestMapping("/valid/code")
    ResultEntity<Boolean> validCode(@RequestParam("code") String code,@RequestParam("memberId") Integer memberId);

    /**
     * 更新认证状态
     * @param memberPO
     * @return
     */
    @RequestMapping("/update/auth/status")
    ResultEntity<Integer> updateAuthStatus(@RequestBody MemberPO memberPO);

    /**
     * 查询审核状态
     * @param memberId
     * @return
     */
    @RequestMapping("/get/auth/status")
    ResultEntity<MemberPO> getAuthStatus(@RequestParam("memberId") Integer memberId);

    /**
     * 查询支付订单的信息
     * @param projectId
     * @param returnId
     * @return
     */
    @RequestMapping("/get/order/project/vo/Remote")
    ResultEntity<OrderProjectVO> getOrderProjectVoRemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);

    /**
     * 根据用户Id查询当前登录用户的收件地址信息
     * @param memberId
     * @return
     */
    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVoRemote(@RequestParam("memberId") Integer memberId);

    /**
     * 添加一个新地址
     * @param addressVO
     * @return
     */
    @RequestMapping("add/new/address/remote")
    ResultEntity<String> addNewAddressRemote(@RequestBody AddressVO addressVO);

    /**
     * 保存支付订单
     * @param orderVO
     * @param memberId
     * @return
     */
    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO ,@RequestParam("memberId") Integer memberId);

    /**
     * 查询我众筹的项目
     * @param memberId
     * @return
     */
    @RequestMapping("/get/my/crowd")
    ResultEntity<List<MyCrowdInfo>> getMyCrowd(@RequestParam("memberId") Integer memberId);
}
