package com.baichen.search.dao;

import com.baichen.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Program: ArticleSearchDao
 * @Author: baichen
 * @Description: dao
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article, String> {
    /**
     * 从字段 title  和 content 搜索文章
     *
     * @param keywords
     * @param keywords1
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String keywords, String keywords1, Pageable pageable);
}
