package com.baichen.friend.service;

import com.baichen.friend.dao.FriendDao;
import com.baichen.friend.dao.NoFriendDao;
import com.baichen.friend.pojo.Friend;
import com.baichen.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Program: FriendService
 * @Author: baichen
 * @Description: 业务逻辑类
 */
@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //判断用户是否已经添加了这个好友，如果count大于0，即已经添加过,直接返回0
        if (friendDao.selectCount(userid, friendid) > 0) {
            return 0;
        }
        //直接添加好友，向好友表中添加记录，让好友表中userid到friendid方向的type为0
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断对方是否喜欢你(两者id要反过来)，如果喜欢，将isLike设置为1
        if (friendDao.selectCount(friendid, userid) > 0) {
            friendDao.updateLike(userid, friendid, "1");
            friendDao.updateLike(friendid, userid, "1");
        }
        return 1;
    }

    /**
     * 向不喜欢列表中添加记录
     *
     * @param userid
     * @param friendid
     */
    public void addNoFriend(String userid, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     *
     * @param userid
     * @param friendid
     */
    public void deleteFriend(String userid, String friendid) {
        // 删除好友表中userid到friendid这条数据
        friendDao.deleteFriend(userid, friendid);
        // 更新好友表中userid到friendid这条数据
        friendDao.updateLike(friendid, userid, "0");
//        非好友表中添加数据
        addNoFriend(userid, friendid);//向不喜欢表中添加记录
    }
}