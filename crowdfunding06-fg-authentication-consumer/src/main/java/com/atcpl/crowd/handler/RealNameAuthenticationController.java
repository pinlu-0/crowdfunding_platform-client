package com.atcpl.crowd.handler;

import com.atcpl.crowd.api.MySQLRemoteService;
import com.atcpl.crowd.config.OSSProperties;
import com.atcpl.crowd.constant.CrowdConstant;
import com.atcpl.crowd.entity.po.CertPO;
import com.atcpl.crowd.entity.po.MemberCert;
import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.vo.MemberLoginVO;
import com.atcpl.crowd.util.CrowdUtil;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.handler
 * @ClassName：AuthController
 * @Date：2023/4/13 13:35
 * @Version：1.0.0
 * @Description TODO(实名认证控制器)
 */
@Controller
public class RealNameAuthenticationController {


    /**
     * 调用远程myslq接口
     */
    @Autowired
    MySQLRemoteService mySqlRemoteService;


    /**
     * OSS服务属性
     */
    @Autowired
    OSSProperties ossProperties;


    /**
     * 完成账户选择
     *
     * @return
     */
    @RequestMapping("/check/account/type.html")
    public String finishCheckAccountType(MemberPO memberPO, HttpSession session) {
        // 注意：往session存实体类需要将实体类实例化
        session.setAttribute(CrowdConstant.AUTH_INFO, memberPO);
        return "authentication-page";
    }

    /**
     * 点击申请认证，并来到基本信息页面
     *
     * @return
     */
    @RequestMapping("/to/basic/information.html")
    public String toBasicInformationPage() {
        // 到基本信息页
        return "tabs/basic-information";
    }

    /**
     * 上传基本信息，并来到资质上传页面
     * TODO(保存基本信息)
     *
     * @return
     */
    @RequestMapping("/to/cert/upload.html")
    public String toCertUpload(MemberPO memberPO, HttpSession session, Model model) {
        MemberPO auth_info = (MemberPO) session.getAttribute("auth_info");

        auth_info.setRealname(memberPO.getRealname());
        auth_info.setCardnum(memberPO.getCardnum());

        // 从登录信息中心获取当前登录的会员id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute("member");
        System.out.println(memberLoginVO);
        auth_info.setId(memberLoginVO.getId());
        System.out.println(auth_info);
        // 调用远程接口上传基本信息
        ResultEntity<Integer> i = mySqlRemoteService.updateMemberInfoRemote(auth_info);

        if (i.getData() != 0) {
            // 查询基本信息成功后，查出所需的资质
            Integer accttypeId = auth_info.getAccttype();
            // 根据accttypeId查询出该账户类型所需的资质
            ResultEntity<List<CertPO>> resultEntity = mySqlRemoteService.getAllCertByAccountTypeId(accttypeId);
            if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
                List<CertPO> list = resultEntity.getData();
                model.addAttribute("certs", list);
            }
            return "tabs/cert-upload";
        } else {
            return "to/basic/information.html";
        }

    }

    /**
     * 上传资质文件，并来到邮箱确认页面
     *
     * @return
     */
    @RequestMapping("/to/email/confirm.html")
    public String toEmailConfirm(@RequestParam("file") MultipartFile[] file, @RequestParam("certid") Integer[] certid, HttpSession session, Model model) throws IOException {

        // 从登录信息中心获取当前登录的会员id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute("member");
        Integer memberid = memberLoginVO.getId();

        model.addAttribute("memberid", memberid);
        List<MemberCert> list = new ArrayList<>();
        for (int i = 0; i < certid.length; i++) {
            MemberCert memberCert = new MemberCert();
            MultipartFile multipartFile = file[i];
            Integer id = certid[i];

            // 调用上传方法，将文件上传到OSS服务器上
            ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOSS(ossProperties.getEndPoint(),
                    ossProperties.getBucketName(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    ossProperties.getBucketDoMain(),
                    multipartFile.getInputStream(),
                    multipartFile.getOriginalFilename());
            memberCert.setCertid(id);
            memberCert.setMemberid(memberid);
            memberCert.setUrl(resultEntity.getData());
            list.add(memberCert);
        }
        // 调用业务逻辑
        ResultEntity<Integer> resultEntity = mySqlRemoteService.upload(list);
        return "tabs/email-confirm";
    }

    /**
     * @return
     */
    @RequestMapping("/to/apply/confirm.html")
    public String toApplyEmail(@RequestParam("email") String email, HttpSession session) {
        // 获取当前登录对象
        MemberLoginVO member = (MemberLoginVO) session.getAttribute("member");
        member.setEmail(email);
        // 调用远程方法
        ResultEntity<Integer> entity = mySqlRemoteService.sendEmail(member);
        return "tabs/apply-confirm";
    }

    /**
     * 校验验证码
     *
     * @param code
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/apply/success.html")
    public ResultEntity<Boolean> applySuccess(String code, HttpSession session) {

        // 收到验证码
        MemberLoginVO member = (MemberLoginVO) session.getAttribute("member");
        ResultEntity<Boolean> entity = mySqlRemoteService.validCode(code, member.getId());

        // 修改认证状态
        if (entity.getData()) {
            MemberPO memberPO = new MemberPO();
            memberPO.setId(member.getId());
            memberPO.setAuthstatus(1);
            ResultEntity<Integer> authStatus = mySqlRemoteService.updateAuthStatus(memberPO);
        }
        return entity;
    }


    /**
     * 来到个人中心页面
     * @param session
     * @return
     */
    @RequestMapping("/fg/auth/to/center/page.html")
    public String getAuthStatus(HttpSession session){

        // 获取当前登录的对象
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute("member");

        // 获取当前登录的对象的id
        Integer memberId = memberLoginVO.getId();

        // 获取档期登录会员的实名认证状态
        ResultEntity<MemberPO> resultEntity = mySqlRemoteService.getAuthStatus(memberId);
        MemberPO memberPO = resultEntity.getData();
        MemberLoginVO memberLoginVO1 = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail(), memberPO.getAuthstatus());
        // 将该状态存入session中
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO1);

        return  "member-center";
    }

}
