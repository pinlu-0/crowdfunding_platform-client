package com.atcpl.crowd.service.impl;

import com.atcpl.crowd.entity.po.*;
import com.atcpl.crowd.mapper.CertPOMapper;
import com.atcpl.crowd.mapper.MemberCertMapper;
import com.atcpl.crowd.mapper.MemberPOMapper;
import com.atcpl.crowd.mapper.MemberTicketMapper;
import com.atcpl.crowd.service.MemberTicketService;
import com.atcpl.crowd.service.RealNameAuthenticationService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.service.impl
 * @ClassName：RealNameAuthenticationImpl
 * @Date：2023/4/14 12:43
 * @Version：1.0.0
 * @Description TODO(实名认证接口实现层)
 */
@Service
public class RealNameAuthenticationServiceImpl implements RealNameAuthenticationService {

    @Autowired
    MemberPOMapper memberPOMapper;


    @Autowired
    CertPOMapper certPOMapper;

    @Autowired
    MemberCertMapper memberCertMapper;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    MemberTicketMapper memberTicketMapper;

    @Autowired
    MemberTicketService memberTicketService;

    @Override
    public Integer updateMemberInfo(MemberPO memberPO) {
        return memberPOMapper.updateByPrimaryKeySelective(memberPO);
    }

    @Override
    public List<CertPO> getAllCertByAccountTypeId(Integer accountTypeId) {
        List<CertPO> certPOS = certPOMapper.selectCertByAccttypeId(accountTypeId);
        return certPOS;
    }

    @Override
    public Integer insertBatch(List<MemberCert> list) {
        // 先删除
        MemberCertExample example = new MemberCertExample();
        MemberCertExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(list.get(0).getMemberid());
        memberCertMapper.deleteByExample(example);
        // 再添加
        return memberCertMapper.insertBatch(list);
    }


    @Override
    public void sendEmail(String email, String username, Integer memberId) {

        //查询流程定义信息
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionName("实名认证流程")
                .latestVersion()
                .singleResult();

        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        Map<String, Object> map = new HashMap<>();
        // 接收验证码的邮箱
        map.put("userEmail", email);
        // 用户名
        map.put("userName", username);
        // 验证码
        map.put("code", code);

        // 获取一个流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId(), map);


        // 创建一个当前用户的工单
        MemberTicket ticket = new MemberTicket();
        ticket.setMemberid(memberId);
        ticket.setTicketid(processInstance.getId());
        ticket.setTickettype("realName");

        memberTicketMapper.insertSelective(ticket);

    }


    @Override
    public boolean validCode(String code, Integer memberId) {

        try {
            MemberTicket memberTicket = memberTicketService.getAuthTicket(memberId);

            // 根据工单表中的ticketID获取任务
            Task task = taskService.createTaskQuery().processInstanceId(memberTicket.getTicketid()).singleResult();

            // 完成任务
            Map<String, Object> variable = new HashMap<>();
            variable.put("usercode", code);
            taskService.complete(task.getId(), variable);


            // 下一个任务
            Task task2 = taskService.createTaskQuery().processInstanceId(memberTicket.getTicketid()).singleResult();
            String task2Name = task2.getName();
            if(task.getName().equals(task2.getName())){
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Integer updateAuthStatus(MemberPO memberPO) {

        int i = memberPOMapper.updateByPrimaryKeySelective(memberPO);

        return i;
    }


    @Override
    public MemberPO getAuthStatus(Integer memberId) {
        return memberPOMapper.selectByPrimaryKey(memberId);
    }
}
