package com.itheIma.service;

import com.itheIma.entity.Result;

import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/09/19 21:18
 **/
public interface OrderService {


    /**
     * 体检预约
     * @param map
     * @return
     * @throws Exception
     */
    Result order(Map map) throws Exception;



    /**
     * 根据id查询预约信息，包括体检人信息、套餐信息
     * @param id
     * @return
     * @throws Exception
     */
    Map findById(Integer id) throws Exception;

}
