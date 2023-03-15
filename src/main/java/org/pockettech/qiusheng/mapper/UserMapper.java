package org.pockettech.qiusheng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pockettech.qiusheng.entity.User;

@Mapper
public interface UserMapper {
    void updateUser(User user);

}
