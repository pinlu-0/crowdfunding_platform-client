package com.atcpl.crowd.service.impl;

import com.atcpl.crowd.entity.po.MemberConfirmInfoPO;
import com.atcpl.crowd.entity.po.MemberLaunchInfoPO;
import com.atcpl.crowd.entity.po.ProjectPO;
import com.atcpl.crowd.entity.po.ReturnPO;
import com.atcpl.crowd.entity.vo.*;
import com.atcpl.crowd.mapper.*;
import com.atcpl.crowd.service.ProjectProvideService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author cpl
 * @date 2023/1/9
 * @apiNote
 */
@Transactional(readOnly = true)
@Service
public class ProjectProvideServiceImpl implements ProjectProvideService {

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    ReturnPOMapper returnPOMapper;

    /**
     * 发布项目 逻辑层
     *
     * @param projectVO
     * @param memberId  memberID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {
        // 一、保存projectPO对象
        // 1.初始化一个ProjectPO对象
        ProjectPO projectPO = new ProjectPO();
        // 2.使用BeanUtils工具将projectVO中的属性复制到projectPO中
        BeanUtils.copyProperties(projectVO, projectPO);
        // 3.给projectPO设置memberId
        projectPO.setMemberid(memberId);
        // 4.给projectPO设置项目创建时间
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createDate);
        // 项目筹集总天数
        Integer day = projectPO.getDay();
        // 设置项目截止时间
        LocalDate deployDate = LocalDate.now().plusDays(day);
        projectPO.setDeploydate(deployDate);
        // 5.设置status=0，表示项目即将开始
        projectPO.setStatus(0);
        // 6.向数据库保存ProjectPO
        // 为了在ProjectPO得到自增的主键，
        // 在mapper的xml文件中对应的insert标签增加了useGeneratedKeys="true" keyProperty="id"的配置
        projectPOMapper.insertSelective(projectPO);


        // 二、保存项目、分类的关联关系
        // 1.获取TypeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        // 2.获取projectId
        Integer projectId = projectPO.getId();
        // 3.执行保存
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        // 三、保存项目、标签的关联关系
        // 1.获取TagIdList
        List<Integer> tagIdList = projectVO.getTagIdList();
        // 2.执行保存
        projectPOMapper.insertTagRelationship(tagIdList, projectId);

        // 四、保存项目详情图片路径信息
        // 1.获取detailPicturePathList
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        // 2.执行保存操作
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        // 五、保存项目发起人信息
        // 1.得到发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        // 2.初始化MemberLaunchInfoPO
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        // 3.给MemberLaunchInfoPO赋值
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        // 4.设置MemberLaunchInfoPO的memberId
        memberLaunchInfoPO.setMemberid(memberId);
        // 5.保存发起人信息
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 六、保存项目回报信息
        // 1.得到项目回报信息的List
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        // 2.初始化一个ReturnPO的list
        List<ReturnPO> returnPOList = new ArrayList<>();
        // 3.遍历给ReturnPO赋值 并存入List
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        }
        // 4.将returnPOList存入数据库
        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        // 七、保存确认信息
        // 1.得到MemberConfirmInfoVO
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();

        // 2.初始化MemberConfirmInfoPO对象
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();

        // 3.给MemberConfirmInfoPO赋值
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);

        // 4.给MemberConfirmInfoPO设置memberId
        memberConfirmInfoPO.setMemberid(memberId);

        // 5.将MemberConfirmInfoPO存入数据库
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    /**
     * 展示项目 逻辑层
     *
     * @return
     */
    @Override
    public List<PortalTypeVO> getPortalTypeVO() {

        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 根据项目id获取detailProjectVO对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        // 通过detailProjectVO对象获取项目审核状态
        Integer status = detailProjectVO.getStatus();

        // 设置项目状态
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("筹集中");
                break;
            case 2:
                detailProjectVO.setStatusText("筹集成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }

        // 根据deplotDate计算lastDay
        String deployDate = detailProjectVO.getDeployDate();

        // 获取当前日期
        Date currentDay = new Date();
        // 设置日期格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //
            Date deployDay = format.parse(deployDate);

            // 获取当期时间日期时间戳；单位为毫秒
            long currentTimeStamp = currentDay.getTime();

            // 获取众筹日期时间戳
            long deployDayTimeStamp = deployDay.getTime();

            // 两个时间戳相减计算已经过去的时间
            long pastDays = (currentTimeStamp - deployDayTimeStamp) / 1000 / 60 / 60 / 24;

            // 获取筹集的总天数
            Integer day = detailProjectVO.getDay();

            // 使用总的众筹天数减去已经过去的天数
            Integer lastDay = (int) (day-pastDays);

            detailProjectVO.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
            }
        return detailProjectVO;
    }
}
