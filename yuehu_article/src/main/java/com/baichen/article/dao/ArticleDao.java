package com.baichen.article.dao;

import com.baichen.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    /**
     * 审核文章，修改状态即可
     *
     * @param articleId
     */
    @Modifying      // 增删改需要加上这个注解
    @Query("update Article set state='1' where id=?1")
    // 这里的1对应方法中参数的个数和位置
    void examine(String articleId);

    /**
     * 点赞文章
     *
     * @param articleId
     */
    @Modifying
    // 做算术运算时，要注意先给字段赋值，否则如果是null的话，运算结果还是null
    @Query("update Article a set a.thumbup=(a.thumbup+1) where a.id=?1")
    void thumbup(String articleId);
}
