package com.atcpl.crowd.handler;

import com.atcpl.crowd.api.MySQLRemoteService;
import com.atcpl.crowd.config.OSSProperties;
import com.atcpl.crowd.constant.CrowdConstant;
import com.atcpl.crowd.entity.vo.*;
import com.atcpl.crowd.util.CrowdUtil;
import com.atcpl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cpl
 * @date 2023/1/16
 * @apiNote
 */
@Controller
public class ProjectController {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    /**
     * 根据项目id查询项目详情信息
     * @param projectId
     * @param model
     * @return
     */
    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model){
        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            DetailProjectVO detailProjectVO = resultEntity.getData();
            model.addAttribute("detailProjectVO", detailProjectVO);
        }
        return "project-detail";
    }


    @ResponseBody
    @RequestMapping("/create/confirm.json")
    public ResultEntity<String> confirmInfo(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO) {
        // 从session域中获取之前存储的projectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_INTERIM_PROJECT);
        // 如果projectVO对象为空，抛出异常
        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_INTERIM_PROJECT_MISSING);
        }
        // 将确认信息数据设置到projectVO对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 从session域中获取当前登录的member
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 调用远程方法保存projectVO对象
        ResultEntity<String> confirmInfoResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO, memberLoginVO.getId());
        if (ResultEntity.FAILED.equals(confirmInfoResultEntity.getResult())) {
            return ResultEntity.failed(confirmInfoResultEntity.getErrorMessage());
        }
        // 将临时的projectVO对象从session域中删除
        session.removeAttribute(CrowdConstant.ATTR_NAME_INTERIM_PROJECT);
        return confirmInfoResultEntity;
    }


    /**
     * 保存回报信息
     *
     * @param returnVO : 保存信息的实体类，用来接收前端发送的表单信息
     * @param session  ： 从session中获取之前保存的projectVO对象
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveRetribution(ReturnVO returnVO, HttpSession session) {
        try {
            // 从session域中读取之前缓存的projectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_INTERIM_PROJECT);
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_INTERIM_PROJECT_MISSING);
            }
            // 从project对象中获取存储汇报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            // 判断returnVOList集合是否为空
            if (returnVOList == null || returnVOList.size() == 0) {
                // 新实例化一个returnVOList对象
                returnVOList = new ArrayList<>();
                // 将这个对象在赋值给ProjectVO类里的returnVO属性
                projectVO.setReturnVOList(returnVOList);
            }
            returnVOList.add(returnVO);
            // 将projectVO存入Session域中
            session.setAttribute(CrowdConstant.ATTR_NAME_INTERIM_PROJECT, projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


    /**
     * 回报信息中的上传图片
     *
     * @param returnPicture ：前端上传的图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadRetribution(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        // 执行文件上传
        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOSS(ossProperties.getEndPoint(),
                ossProperties.getBucketName(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketDoMain(),
                returnPicture.getInputStream(),
                returnPicture.getOriginalFilename());
        // 返回上传的结果
        return resultEntity;
    }


    /**
     * 保存发起项目及发起人详细信息
     *
     * @param projectVO         : 接收除了上传图片以外的其它普通数据
     * @param headerPicture     : 接收上传的头图
     * @param detailPictureList ： 接收上传的详情图片
     * @param session           : 用来将收集了一部分数据的ProjectVO对象存入Session域中
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/item/information.json")
    public ResultEntity<String> saveProjectBasicInfo(ProjectVO projectVO, MultipartFile headerPicture, List<MultipartFile> detailPictureList, HttpSession session) throws IOException {
        // ********************************************上传头图
        boolean empty = headerPicture.isEmpty();
        // 如果头图为空
        if (empty) {
            //return "project-launch";
            return ResultEntity.failed(CrowdConstant.MESSAGE_HEADER_PICTURE_IS_REQUIRE);
        }
        // 执行上传
        ResultEntity<String> headerPictureResultEntity = CrowdUtil.uploadFileToOSS(ossProperties.getEndPoint(),
                ossProperties.getBucketName(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketDoMain(),
                headerPicture.getInputStream(),
                headerPicture.getOriginalFilename());
        // 判断是否上传成功
        if (ResultEntity.SUCCESS.equals(headerPictureResultEntity.getResult())) {
            // 获取数据中头图的访问路径
            String headerPicturePath = headerPictureResultEntity.getData();
            // 存入ProjectVO中
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            // 失败情况
            //return "project-launch";
            return ResultEntity.failed(CrowdConstant.MESSAGE_HEADER_PICTURE_UPLOAD_FAILED);
        }


        // ******************************************上传详情图片
        // 创建一个List存放详情图片的路径
        List<String> detailPicturePathList = new ArrayList<>();
        // 判断detailPictureList集合是否为空
        if (detailPictureList.size() == 0 || detailPictureList == null) {
            //return "project-launch";
            return ResultEntity.failed(CrowdConstant.MESSAGE_DETAIL_PICTURE_IS_REQUIRE);
        }
        // 遍历detailPicturePathList集合获取详情图片路径
        for (MultipartFile detailPicture : detailPictureList) {
            // detailPicture为空
            if (detailPicture.isEmpty()) {
                // 检测到单个详情图片为空也要跳转回去显示错误消息
                //return "project-launch";
                return ResultEntity.failed(CrowdConstant.MESSAGE_DETAIL_PICTURE_IS_REQUIRE);
            }
            // detailPicture不为空
            if (!detailPicture.isEmpty()) {
                // 执行上传
                ResultEntity<String> detailPictureResultEntity = CrowdUtil.uploadFileToOSS(ossProperties.getEndPoint(),
                        ossProperties.getBucketName(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        ossProperties.getBucketDoMain(),
                        detailPicture.getInputStream(),
                        detailPicture.getOriginalFilename());
                String result = detailPictureResultEntity.getResult();
                // 检查上传结果
                if (ResultEntity.SUCCESS.equals(result)) {
                    String detailPicturePath = detailPictureResultEntity.getData();
                    // 将图片路径存入集合中
                    detailPicturePathList.add(detailPicturePath);
                } else {
                    //return "project-launch";
                    return ResultEntity.failed(CrowdConstant.MESSAGE_DETAIL_PICTURE_UPLOAD_FAILED);
                }
            }
        }
        // 将detailPicturePathList集合存入ProjectVO中
        projectVO.setDetailPicturePathList(detailPicturePathList);
        // 将ProjectVO存入session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_INTERIM_PROJECT, projectVO);
        // 重定向到回报信息页面
        // return "redirect:http://localhost/project/return/info/page.html";
        return ResultEntity.successWithoutData();
    }
}
