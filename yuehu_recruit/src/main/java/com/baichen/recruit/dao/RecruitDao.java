package com.baichen.recruit.dao;

import com.baichen.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author  baichen
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    /**
     * 推荐职位,查询最新职位列表(按创建日期降序排序),前4条记录
     */
    List<Recruit> findTop4ByStateOrderByCreatetimeDesc(String state);

    /**
     * 最新职位,查询状态不为0并以创建日期降序排序，查询前12条记录
     */
    List<Recruit> findTop12ByStateNotOrderByCreatetimeDesc(String s);
}
