package org.pockettech.qiusheng.dao;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.data.User;

public interface UserDao {
    User findNameByUid(@Param("uid") int uid);
    User findUserByName(@Param("user_name") String user_name);
    void uploadUser(User user);
    void updateUserById(@Param("uid") int uid, @Param("user_name") String user_name);
}
