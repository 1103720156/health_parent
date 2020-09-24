package com.itheIma.dao;

import com.itheIma.pojo.Role;

import java.util.Set;

/**
 * @Author 意风秋
 * @Date 2020/09/22 17:33
 **/
public interface RoleDao {
    Set<Role> findByUserId(Integer t_userId);
}
