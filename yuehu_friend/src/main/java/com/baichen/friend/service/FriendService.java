package com.baichen.friend.service;

import com.baichen.friend.dao.FriendDao;
import com.baichen.friend.pojo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Program: FriendService
 * @Author: baichen
 * @Description: 业务逻辑类
 */
@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    @Transactional
    public int addFriend(String userid, String friendid) {
        //判断如果用户已经添加了这个好友，则不进行任何操作,返回0
        if (friendDao.selectCount(userid, friendid) > 0) {
            return 0;
        }
        //向喜欢表中添加记录
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIsLike("0");
        friendDao.save(friend);
        //判断对方是否喜欢你，如果喜欢，将isLike设置为1
        if (friendDao.selectCount(friendid, userid) > 0) {
            friendDao.updateLike(userid, friendid, "1");
            friendDao.updateLike(friendid, userid, "1");
        }
        return 1;
    }
}
