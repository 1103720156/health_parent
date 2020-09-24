package com.itheIma.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheIma.constant.MessageConstant;
import com.itheIma.dao.MemberDao;

import com.itheIma.dao.OrderDao;
import com.itheIma.dao.OrderSettingDao;
import com.itheIma.entity.Result;
import com.itheIma.pojo.Member;
import com.itheIma.pojo.Order;
import com.itheIma.pojo.OrderSetting;
import com.itheIma.service.OrderService;
import com.itheIma.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/09/19 21:19
 **/
@Service( interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //检查预约日期是否预约已满 i
        int number = orderSetting.getNumber();
        //可预约人数
        int reservations = orderSetting.getReservations();
        //已预约人数
        if(reservations >= number){
            //预约已满，不能预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //防止重复预约
        if(member != null){
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED); }
        }
        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        if(member == null){
            //当前用户不是会员，需要添加到会员表
            member = new Member(); member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));

        member.setRegTime(new Date());
        memberDao.add(member);
        }
        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                         date,
                (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id); if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }


}

