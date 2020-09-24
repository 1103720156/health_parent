package com.itheIma.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheIma.dao.OrderSettingDao;
import com.itheIma.entity.Result;
import com.itheIma.pojo.OrderSetting;
import com.itheIma.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Author 意风秋
 * @Date 2020/08/27 18:23
 * @Creed 这一页的代码我看不懂
 **/

@Service(interfaceClass=OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        if(list !=null && list.size()>0){
            for(OrderSetting orderSetting:list){
                //检查日期是否存在
                long count=orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());


                if(count > 0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
//        String dateBegin = date + "‐1";//2020-8-1
//        String dateEnd = date + "‐31";//2020-8-31
        String[] sourceStrArray = date.split("-");
        String dateBegin= sourceStrArray[0];
        String  dateEnd=sourceStrArray[1];
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            // 获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());
            //可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());
            // 已预约人数

            data.add(orderSettingMap);
        }

        return data;

    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }

}
