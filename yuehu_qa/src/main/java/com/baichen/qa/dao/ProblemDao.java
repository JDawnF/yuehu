package com.baichen.qa.dao;

import com.baichen.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 分页查询最新问答列表，根据标签ID查询最新问题列表，每个labelId对应一个问题列表
     * 回答时间倒序
     * @param labelId
     * @param pageable
     * @return
     * 加上nativeQuery = true表示是原生sql，如果不加则其中的select * from 的xxx中xxx也不是数据库对应的真正的表名，
     * 而是对应的实体名，并且sql中的字段名也不是数据库中真正的字段名，而是实体的字段名。
     */
    @Query(value = "select * from tb_problem,tb_pl where id = problemid and labelid=? order by replytime desc",nativeQuery = true)
    Page<Problem> findNewListByLabelId(String labelId, Pageable pageable);


    /**
     * 分页查询热门回答列表,按回复数降序排序
     * @param labelId
     * @param pageable
     * @return
     */
//    @Query(value = "select p from Problem p where id in( select problemid from tb_pl where labelid=?1 ) order by p.reply desc",nativeQuery = true)
    @Query(value = "select * from tb_problem,tb_pl where id=problemid and labelid=? order by reply desc",nativeQuery = true)
    Page<Problem> findHotListByLabelId(String labelId, Pageable pageable);


    /**
     * 根据标签ID查询等待回答列表
     * @param labelId
     * @param pageable
     * @return
     */
//    @Query(value = "select p from Problem p where p.id in (select problemid from tb_pl where labelid=?1) and p.reply=0 order by p.createtime desc",nativeQuery = true)
    @Query(value = "select * from tb_problem,tb_pl where id=problemid and labelid=? and reply=0 order by createtime desc",nativeQuery = true)
    Page<Problem> findWaitListByLabelId(String labelId, Pageable pageable);
}
