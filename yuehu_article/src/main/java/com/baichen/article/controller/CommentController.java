package com.baichen.article.controller;

import com.baichen.article.pojo.Comment;
import com.baichen.article.service.CommentService;
import com.baichen.entity.Contants;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Program: CommentController
 * @Author: baichen
 * @Description: 评论controller
 */
@Controller
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment) {
        commentService.add(comment);
        return new Result(true, StatusCode.OK, Contants.SUBMIT_SUCCESS);
    }
    @RequestMapping(value="/article/{articleid}",method= RequestMethod.GET)
    public Result findByArticleid(@PathVariable String articleid){
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, commentService.findByArticleid(articleid));
    }
}
