package com.itheIma.dao;

import com.itheIma.pojo.User;

/**
 * @Author 意风秋
 * @Date 2020/09/22 17:21
 **/
public interface UserDao {
    User findByUsername(String username);
}
