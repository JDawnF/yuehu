package com.baichen.article.controller;


import com.baichen.article.pojo.Article;
import com.baichen.article.service.ArticleService;
import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 控制器层
 *
 * @author  baichen
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, articleService.findAll());
    }

    /**
     * 根据ID查询，在service层中放到Redis中
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Article article = articleService.findById(id);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, article);
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Article> pageList = articleService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, articleService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param article
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    /**
     * 修改
     *
     * @param article
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Article article, @PathVariable String id) {
        article.setId(id);
        articleService.update(article);
        return new Result(true, StatusCode.OK, Contants.UPDATE_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        articleService.deleteById(id);
        return new Result(true, StatusCode.OK, Contants.DELETE_SUCCESS);
    }


    /**
     * 审核文章
     *
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/examine/{articleId}", method = RequestMethod.PUT)
    public Result examine(@PathVariable String articleId) {
        articleService.examine(articleId);
        return new Result(true, StatusCode.OK, Contants.EXAMINE_SUCCESS);
    }


    /**
     * 文章点赞
     *
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/thumbup/{articleId}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String articleId) {
        articleService.thumbup(articleId);
        return new Result(true, StatusCode.OK, Contants.THUMBUP_SUCCESS);
    }

    /**
     * 文章评论
     * @param articleId
     * @return
     */
//    @RequestMapping(value = "/comment/{articleId}",method = RequestMethod.POST)
//    public Result addComment(@PathVariable String articleId, @RequestBody Comment comment){
//        articleService.addComment(articleId,comment);
//        return new Result(true,StatusCode.OK,"增加评论成功");
//    }
}
