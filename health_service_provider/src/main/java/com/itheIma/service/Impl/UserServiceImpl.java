package com.itheIma.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheIma.dao.PermissionDao;
import com.itheIma.dao.RoleDao;
import com.itheIma.dao.UserDao;
import com.itheIma.pojo.Permission;
import com.itheIma.pojo.Role;
import com.itheIma.pojo.User;
import com.itheIma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Set;

/**
 * @Author 意风秋
 * @Date 2020/09/22 17:20
 **/
@Service( interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 查询封装相应权限
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user=userDao.findByUsername(username);

        if(user == null){
            return null;
        }
        Integer userId = user.getId();

        Set<Role> roles = roleDao.findByUserId(userId);
        if(roles != null && roles.size() > 0){

            //foreach可以操作对象容器，不能操作变量容器，不能操作变量
            for(Role role:roles){
                Integer roleId = role.getId();
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if (permissions != null && permissions.size() > 0) {
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }

        return user;
    }
}
