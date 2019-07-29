package com.baichen.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @Program: UserClient
 * @Author: baichen
 * @Description: 用户客户端，调用user服务
 */
@FeignClient("yuehu-user")
public interface UserClient {
    /**
     * 更新好友粉丝数和用户关注数
     * 注意PathVariable里面要加上参数变量
     */
    @PutMapping(value = "/user/{userid}/{friendid}/{x}")
    public void updateFansCountAndFollowCount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("x") int x);
}
