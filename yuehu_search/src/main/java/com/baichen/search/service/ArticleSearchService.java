package com.baichen.search.service;

import com.baichen.search.dao.ArticleSearchDao;
import com.baichen.search.pojo.Article;
import com.baichen.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Program: ArticleSearchService
 * @Author: baichen
 * @Description: service
 */
@Service
public class ArticleSearchService {
    @Autowired
    private ArticleSearchDao articleSearchDao;
//    @Autowired
//    private IdWorker idWorker;

    /**
     * 增加文章
     *
     * @param article
     */
    public void add(Article article) {
//        article.setId(idWorker.nextId() + "");    // 可以用es自带的生成的id
        articleSearchDao.save(article);
    }

    /**
     * 搜索文章
     *
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findSearch(String keywords, int page, int size) {
        return articleSearchDao.findByTitleOrContentLike(keywords, keywords, PageRequest.of(page - 1, size));
    }
}
