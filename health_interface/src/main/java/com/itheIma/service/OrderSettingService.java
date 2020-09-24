package com.itheIma.service;

import com.itheIma.entity.Result;
import com.itheIma.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/27 18:12
 * @Creed 这一页的代码我看不懂
 **/
public interface OrderSettingService {
    void add(List<OrderSetting> list);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);

}
