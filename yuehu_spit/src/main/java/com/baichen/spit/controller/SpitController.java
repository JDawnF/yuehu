package com.baichen.spit.controller;

import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.spit.pojo.Spit;
import com.baichen.spit.service.SpitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    //增加
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit) {
        spitService.add(spit);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    //全部列表
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, spitService.findAll());
    }

    //根据ID查询
    @RequestMapping(value = "/{spitId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, spitService.findById(spitId));
    }

    //修改
    @RequestMapping(value = "/{spitId}", method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId, @RequestBody Spit spit) {
        spit.setId(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, Contants.UPDATE_SUCCESS);
    }

    //删除
    @RequestMapping(value = "/{spitId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId) {
        spitService.delete(spitId);
        return new Result(true, StatusCode.OK, Contants.DELETE_SUCCESS);
    }

    //根据上级ID查询吐槽列表(分页)
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pageList = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }


    //点赞,控制不能重复点赞
    @RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String id) {
        //现在还未做登录，先把userid写死，模拟当前登录用户
        String userid = "1";

        //1. 从redis 判断该用户是否已经点赞过
        String flag = (String) redisTemplate.opsForValue().get("thumbup_" + userid + "_" + id);
        if (flag != null) {
            //点赞过
            return new Result(false, StatusCode.REPEATE_ERROR, Contants.O);
        }

        spitService.thumbup(id);
        //2.把数据存入redis
        redisTemplate.opsForValue().set("thumbup_" + userid + "_" + id, "1");

        return new Result(true, StatusCode.OK, Contants.THUMBUP_SUCCESS);
    }

    //浏览量
    @RequestMapping(value = "/visited/{id}", method = RequestMethod.PUT)
    public Result visit(@PathVariable String id) {
        spitService.visit(id);
        return new Result(true, StatusCode.OK, Contants.VISIT_SUCCESS);
    }

    //转发分享
    @RequestMapping(value = "/share/{id}", method = RequestMethod.PUT)
    public Result share(@PathVariable String id) {
        spitService.share(id);
        return new Result(true, StatusCode.OK, Contants.SHARE_SUCCESS);
    }
}
