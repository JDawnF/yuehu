package com.baichen.recruit.dao;

import com.baichen.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>,JpaSpecificationExecutor<Enterprise>{

    /**
     * 根据热门状态获取企业列表
     */
    List<Enterprise> findByIshot(String ishot);     // where ishot=?
}
