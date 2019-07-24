package com.baichen.user.controller;

import com.baichen.entity.Contants;
import com.baichen.entity.PageResult;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.user.pojo.User;
import com.baichen.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author baichen
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, userService.findById(id));
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
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, Contants.SEARCH_SUCCESS, userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(String code, @RequestBody User user) {
        userService.add(code, user);
        return new Result(true, StatusCode.OK, Contants.ADD_SUCCESS);
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, Contants.UPDATE_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, Contants.DELETE_SUCCESS);
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return new Result(true, StatusCode.OK, Contants.SEND_SUCCESS);
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@PathVariable String code, @RequestBody User user) {
        // 得到缓存中的验证码
        String smsCode = (String) redisTemplate.opsForValue().get("smsCode_" + user.getMobile());
        if (smsCode.isEmpty()) {
            return new Result(false, StatusCode.ERROR, Contants.EMPTY_SMSCODE);
        }
        if (!smsCode.equals(code))
            return new Result(false, StatusCode.ERROR, Contants.WRONG_SMSCODE);
        userService.add(code, user);
        return new Result(true, StatusCode.OK, Contants.REGISTER_SUCCESS);
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        User loginUser = userService.login(user.getMobile(), user.getPassword());
        if (loginUser != null) {
//            String token = jwtUtil.createJWT(loginUser.getId(), loginUser.getMobile(), "user");
//            HashMap<String, String> map = new HashMap<>();
//            map.put("name", loginUser.getMobile());
//            map.put("token", token);

            return new Result(true, StatusCode.OK, Contants.LOGIN_SUCCESS);
        }
        return new Result(false, StatusCode.LOGIN_ERROR, Contants.LOGIN_FAILED);
    }

    /**
     * 更新关注数
     *
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/updateFollowcount/{userid}/{x}", method = RequestMethod.PUT)
    public Result updateFollowcount(@PathVariable String userid, @PathVariable int x) {
        userService.updateFollowcount(userid, x);
        return new Result(true, StatusCode.OK, Contants.UPDATE_FOLLOW_SUCCESS);
    }

    /**
     * 更新关注数
     *
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/updateFanswcount/{userid}/{x}", method = RequestMethod.PUT)
    public Result updateFanswcount(@PathVariable String userid, @PathVariable int x) {
        userService.updateFanswcount(userid, x);
        return new Result(true, StatusCode.OK, Contants.UPDATE_FOLLOW_SUCCESS);
    }
}
