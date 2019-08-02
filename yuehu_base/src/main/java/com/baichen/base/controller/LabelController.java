package com.baichen.base.controller;

import com.baichen.base.pojo.Label;
import com.baichen.base.service.LabelService;
import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Program: BaseController
 * @Author: baichen
 * @Description:
 */
@RestController         // 对象转成json
@CrossOrigin
@RequestMapping("/label")
@RefreshScope    // 自动刷新自定义的配置
public class LabelController {
    @Autowired
    private LabelService labelService;

    @Autowired
    private HttpServletRequest request;

    @Value("${ip}")
    private String ip;

    /**
     * 添加标签
     *
     * @param label
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Label label) {
        labelService.add(label);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        System.out.println("在码云上自定义的ip为" + ip);
        String header = request.getHeader("Authorization");
        System.out.println(header);
        List<Label> list = labelService.findAll();
        return new Result(true, StatusCode.OK, "正常", list);
    }

    /**
     * 根据ID查询标签
     *
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId) {
        return new Result(true, StatusCode.OK, "正常", labelService.findById(labelId));
    }

    /**
     * 更新标签
     *
     * @param label
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.PUT)
    public Result update(@RequestBody Label label, @PathVariable String labelId) {
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK, Contants.UPDATE_SUCCESS);
    }

    /**
     * 删除标签
     *
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String labelId) {
        labelService.delete(labelId);
        return new Result(true, StatusCode.OK, Contants.DELETE_SUCCESS);
    }

    /**
     * 条件查询,ResponseBody也可以将json转为Map类型
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result search(@RequestBody Map searchMap) {
        List<Label> labelList = labelService.search(searchMap);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, labelList);
    }

    /**
     * 条件查询+分页
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/searchPage/{page}/{size}", method = RequestMethod.POST)
    public Result searchPage(@PathVariable int page, @PathVariable int size, @RequestBody Map searchMap) {
        Page<Label> labelList = labelService.search(searchMap, page, size);
        // 返回总记录数和总页数
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<Label>(labelList.getTotalElements(), labelList.getContent()));
    }

}
