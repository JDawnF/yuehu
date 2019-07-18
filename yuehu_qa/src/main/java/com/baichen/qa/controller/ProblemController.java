package com.baichen.qa.controller;

import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.qa.pojo.Problem;
import com.baichen.qa.service.ProblemService;
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
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, problemService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, problemService.findById(id));
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
        Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, problemService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param problem
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem) {
        problemService.add(problem);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    /**
     * 修改
     *
     * @param problem
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.update(problem);
        return new Result(true, StatusCode.OK, Contants.UPDATE_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        problemService.deleteById(id);
        return new Result(true, StatusCode.OK, Contants.DELETE_SUCCESS);
    }

    /**
     * 最新问答列表
     *
     * @param labelid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/newlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
    public Result newlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.newlist(labelid, page, size);
        return new Result(true, StatusCode.OK, Contants.NEWEST_ANSWER, new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据标签ID查询热门问题列表
     *
     * @param labelid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/hotlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
    public Result hotlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.hotList(labelid, page, size);
        return new Result(true, StatusCode.OK, Contants.HOT_ANSWER, new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据标签ID查询等待回答列表
     * @param labelid
     * @return
     */
    @RequestMapping(value = "/waitlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
    public Result waitlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.waitList(labelid, page, size);
        return new Result(true, StatusCode.OK, Contants.WAITING_ANSWER, new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

}