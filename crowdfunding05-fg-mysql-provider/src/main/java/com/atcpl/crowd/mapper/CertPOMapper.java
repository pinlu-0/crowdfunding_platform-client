package com.atcpl.crowd.mapper;

import com.atcpl.crowd.entity.po.CertPO;
import com.atcpl.crowd.entity.po.CertPOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertPOMapper {
    int countByExample(CertPOExample example);

    int deleteByExample(CertPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CertPO record);

    int insertSelective(CertPO record);

    List<CertPO> selectByExample(CertPOExample example);

    CertPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CertPO record, @Param("example") CertPOExample example);

    int updateByExample(@Param("record") CertPO record, @Param("example") CertPOExample example);

    int updateByPrimaryKeySelective(CertPO record);

    int updateByPrimaryKey(CertPO record);

    /**
     * 根据账户类型id查询该账户类型所对应的资质
     * @param accttpeId
     * @return
     */
    List<CertPO> selectCertByAccttypeId(Integer accttpeId);
}