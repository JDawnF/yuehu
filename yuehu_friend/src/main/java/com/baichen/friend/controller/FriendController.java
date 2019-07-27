package com.baichen.friend.controller;

import com.baichen.entity.Contants;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.friend.service.FriendService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Program: FriendController
 * @Author: baichen
 * @Description: 控制类
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 添加好友
     *
     * @param friendid 对方用户ID
     * @param type     1:喜欢 0:不喜欢
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid, @PathVariable
            String type) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESS_ERROR, Contants.ACCESS_FAILED);
        }
        //如果是喜欢
        if (type.equals("1")) {
            if (friendService.addFriend(claims.getId(), friendid) == 0) {
                return new Result(false, StatusCode.REP_ERROR, Contants.ALREADY_ADDED);
            }
        }
        return new Result(true, StatusCode.OK, Contants.OPERATE_SUCCESS);
    }
}

