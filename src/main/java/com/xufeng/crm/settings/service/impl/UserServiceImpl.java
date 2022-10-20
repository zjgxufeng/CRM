package com.xufeng.crm.settings.service.impl;

import com.xufeng.crm.settings.domain.User;
import com.xufeng.crm.settings.mapper.UserMapper;
import com.xufeng.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);

    }
}
