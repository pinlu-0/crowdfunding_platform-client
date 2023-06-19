package com.atcpl.crowd.service.impl;

import com.atcpl.crowd.entity.po.MemberPO;
import com.atcpl.crowd.entity.po.MemberPOExample;
import com.atcpl.crowd.mapper.MemberPOMapper;
import com.atcpl.crowd.service.MemberProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cpl
 * @date 2022/12/29
 * @apiNote
 */
@Service
@Transactional(readOnly = true)
public class MemberProviderServiceImpl implements MemberProviderService {
    @Autowired
    private MemberPOMapper memberPOMapper;

    /**
     * 通过loginAcct获取与数据库对应的MemberPO对象
     * @param loginacct
     * @return
     */
    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        MemberPOExample example = new MemberPOExample();

        MemberPOExample.Criteria criteria = example.createCriteria();

        criteria.andLoginacctEqualTo(loginacct);

        List<MemberPO> memberPOList = memberPOMapper.selectByExample(example);
        // 判断List是否为空，为空则返回null，防止后面调用的时候触发空指针异常
        if (memberPOList == null || memberPOList.size() == 0) {
            return null;
        }
        return memberPOList.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveMemberPO(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
