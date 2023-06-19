package com.atcpl.crowd.test;

import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.vo.PortalProjectVO;
import com.atcpl.crowd.entity.vo.PortalTypeVO;
import com.atcpl.crowd.mapper.MemberPOMapper;
import com.atcpl.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author cpl
 * @date 2022/12/29
 * @apiNote
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    private Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Test
    public void test1(){
        List<PortalTypeVO> typeVOList = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalTypeVO : typeVOList){
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.info("name="+name+" "+"remark="+remark);
            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            for (PortalProjectVO portalProjectVO : portalProjectVOList){
                if(portalProjectVO == null){
                    continue;
                }
                logger.info(portalProjectVO.toString());
            }
        }
    }

    @Test
    public void testMapper(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawpassword = "123123";
        String encode = bCryptPasswordEncoder.encode(rawpassword);
        MemberPO memberPO = new MemberPO(null,"狗蛋",encode,"狗蛋","goudan@163.com",0,1,"张狗蛋","411421199901025614",0);


        memberPOMapper.insert(memberPO);
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }
}
