package com.xufeng.crm.settings.service;

import com.xufeng.crm.settings.domain.User;

import java.util.Map;

public interface UserService {

    User queryUserByLoginActAndPwd(Map<String,Object> map);
}
