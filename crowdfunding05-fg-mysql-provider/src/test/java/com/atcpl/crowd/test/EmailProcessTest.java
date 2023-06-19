package com.atcpl.crowd.test;

import com.atcpl.crowd.mapper.MemberCertMapper;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：cpl
 * @Package：com.atcpl.crowd.test
 * @ClassName：EmailProcess
 * @Date：2023/4/17 15:48
 * @Version：1.0.0
 * @Description TODO(这里用一句话描述这个类的作用)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailProcessTest {

    @Autowired
    MemberCertMapper memberCertMapper;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Test
    public void sendEmailTest() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName("实名认证流程")
                .latestVersion()
                .singleResult();
        Map<String, Object> map = new HashMap<>();
        // 接收验证码的邮箱
        map.put("userEmail", "15660129782@163.com");
        // 用户名
        map.put("userName", "张三");
        // 验证码
        map.put("code", "1123");

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId(),map);
    }
}
