package com.baichen.friend.dao;

import com.baichen.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String> {
    /**
     * 根据用户ID与被关注用户ID查询记录个数
     * @param userid    用户id
     * @param friendid  好友id
     * @return
     */
    @Query("select count(f) from Friend f where f.userid=?1 and f.friendid=?2")
    int selectCount(String userid, String friendid);

    /**
     * 更新为互相喜欢
     *
     * @param userid
     * @param friendid
     */
    @Modifying
    // 数字表示的是方法中参数对应的位置
    @Query("update Friend f set f.islike=?3 where f.userid=?1 and f.friendid=?2")
    void updateLike(String userid, String friendid, String islike);

    /**
     * 删除好友，即不喜欢
     * @param userid
     * @param friendid
     * */
    @Modifying
    @Query("delete from Friend f where f.userid=?1 and f.friendid=?2")
    public void deleteFriend(String userid,String friendid);
}
