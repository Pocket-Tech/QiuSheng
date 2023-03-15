package org.pockettech.qiusheng.service.impl;

import org.pockettech.qiusheng.entity.User;
import org.pockettech.qiusheng.mapper.UserMapper;
import org.pockettech.qiusheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}
