package com.baichen.friend.controller;

import com.baichen.entity.Contants;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.friend.client.UserClient;
import com.baichen.friend.service.FriendService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UserClient userClient;

    /**
     * 添加好友或添加非好友
     *
     * @param friendid 对方用户ID
     * @param type     1:喜欢 0:不喜欢,0:单向喜欢 1:互相喜欢
     * @return
     */
    @PutMapping(value = "/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid, @PathVariable String type) {
        // 验证是否登录，并且拿到当前登录的用户id,即验证普通该用户角色
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESS_ERROR, Contants.ACCESS_FAILED);
        }
        //判断是添加好友还是添加非好友
        if (type != null) {
            if ("1".equals(type)) { // 添加好友
                // 已经添加过好友
                if (friendService.addFriend(claims.getId(), friendid) == 0) {
                    return new Result(false, StatusCode.REP_ERROR, Contants.ALREADY_ADDED);
                } else {
                    userClient.updateFansCountAndFollowCount(claims.getId(), friendid, 1);//增加自己的关注数
                    return new Result(true, StatusCode.OK, Contants.ADD_FRIEND_SUCCESS);
                }
            } else if ("2".equals(type)) { // 添加非好友
                friendService.addNoFriend(claims.getId(), friendid);//向不喜欢 列表中添加记录
                return new Result(true, StatusCode.OK, Contants.ADD_NOFRIEND_SUCCESS);
            }
        }
        return new Result(false, StatusCode.ERROR, Contants.ARGS_ILLGAL);
    }

//    删除好友
    @DeleteMapping(value = "/{friendid}")
    public Result deleteFriend(@PathVariable String friendid){
        // 验证是否登录，并且拿到当前登录的用户id,即验证普通该用户角色
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESS_ERROR, Contants.ACCESS_FAILED);
        }
        // 得到当前登录的用户id
        String userId=claims.getId();
        friendService.deleteFriend(userId,friendid);
        userClient.updateFansCountAndFollowCount(userId,friendid,-1);
        return new Result(true,StatusCode.OK,Contants.DELETE_FRIEND_SUCCESS);
    }
}

