package com.baichen.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Program: Friend
 * @Author: baichen
 * @Description: 实体类
 */
@Entity
@Table(name="tb_friend")
@IdClass(Friend.class)      // 复合组件映射
public class Friend implements Serializable {
    @Id
    private String userid;      // 用户id
    @Id
    private String friendid;    // 关注的用户id

    private String islike;

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }
}
