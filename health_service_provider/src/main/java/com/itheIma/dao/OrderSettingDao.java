package com.itheIma.dao;

import com.itheIma.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/27 18:24
 * @Creed 这一页的代码我看不懂
 **/
public interface OrderSettingDao {

    //更新可预约人数
    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    //更新已预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);


    long findCountByOrderDate(Date orderDate);
    //根据日期范围查询预约设置信息
     List<OrderSetting> getOrderSettingByMonth(Map date);


    //根据预约日期查询预约设置信息
    OrderSetting findByOrderDate(Date orderDate);
}
