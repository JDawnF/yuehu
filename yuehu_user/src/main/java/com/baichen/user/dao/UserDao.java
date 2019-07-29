package com.baichen.user.dao;

import com.baichen.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author baichen
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 更新粉丝数
     *
     * @param friendid
     * @param x
     */
    @Modifying
    @Query(value = "update tb_user set followcount = followcount +? where id = ?", nativeQuery = true)
    void updateFanswcount(int x, String friendid);

    /**
     * 更新关注数
     *
     * @param userid
     * @param x
     */
    @Modifying
    @Query("update User u set u.fanscount = u.fanscount + ?1 where u.id = ?2")
    void updateFollowcount(int x, String userid);
}
