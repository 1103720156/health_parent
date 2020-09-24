package com.itheIma.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheIma.constant.MessageConstant;
import com.itheIma.entity.Result;
import com.itheIma.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 意风秋
 * @Date 2020/09/22 22:01
 **/

@RestController
@RequestMapping("/user")
public class UserController {


    /**
     * 查询用户名
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername(){
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
