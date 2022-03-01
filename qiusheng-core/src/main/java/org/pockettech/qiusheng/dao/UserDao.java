package org.pockettech.qiusheng.dao;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.Data.User;

public interface UserDao {
    User findUserByName(@Param("user_name") String user_name);
    void uploadUser(User user);
    void updateUserById(@Param("uid") int uid, @Param("user_name") String user_name);
}
