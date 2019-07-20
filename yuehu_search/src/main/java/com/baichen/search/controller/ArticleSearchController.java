package com.baichen.search.controller;

import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.search.pojo.Article;
import com.baichen.search.service.ArticleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Program: ArticleSearchController
 * @Author: baichen
 * @Description: controller
 */
@RestController
@CrossOrigin
@RequestMapping("/search")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    //    添加文章到elasticsearch
    @PostMapping
    public Result add(@RequestBody Article article) {
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    @GetMapping(value = "/{keywords}/{page}/{size}")
    // PathVariable注解获取value中的keywords
    public Result findSearch(@PathVariable String keywords, @PathVariable int page, @PathVariable int size) {
        Page<Article> pageList = articleSearchService.findSearch(keywords, page, size);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }
}
