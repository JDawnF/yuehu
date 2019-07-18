package com.baichen.recruit.controller;

import java.util.List;
import java.util.Map;

import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.recruit.pojo.Recruit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baichen.recruit.service.RecruitService;


/**
 * 控制器层
 *
 * @author  baichen
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, recruitService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, recruitService.findById(id));
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
        Page<Recruit> pageList = recruitService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<Recruit>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, recruitService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param recruit
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Recruit recruit) {
        recruitService.add(recruit);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    /**
     * 修改
     *
     * @param recruit
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Recruit recruit, @PathVariable String id) {
        recruit.setId(id);
        recruitService.update(recruit);
        return new Result(true, StatusCode.OK, Contants.UPDATE_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        recruitService.deleteById(id);
        return new Result(true, StatusCode.OK, Contants.DELETE_SUCCESS);
    }


    /**
     * 查询前4个推荐职位
     *
     * @return
     */
    @RequestMapping(value = "/search/recommend", method = RequestMethod.GET)
    public Result recommend() {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, recruitService.findTop4ByStateOrderByCreatetimeDesc());
    }


    /**
     * 查询最新职位
     *
     * @return
     */
    @RequestMapping(value = "/search/newlist", method = RequestMethod.GET)
    public Result newlist() {
        return new Result(true, StatusCode.OK, Contants.NEW_RECRUIT, recruitService.newlist());
    }

}
