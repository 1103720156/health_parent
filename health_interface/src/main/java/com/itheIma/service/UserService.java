package com.itheIma.service;

import com.itheIma.pojo.User;

/**
 * @Author 意风秋
 * @Date 2020/09/22 17:16
 **/
public interface UserService {
    User findByUsername(String username);
}
