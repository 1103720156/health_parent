package com.itheIma.dao;

import com.itheIma.pojo.Permission;

import java.util.Set;

/**
 * @Author 意风秋
 * @Date 2020/09/22 17:33
 **/
public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
