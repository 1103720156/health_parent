package com.itheIma.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheIma.constant.MessageConstant;
import com.itheIma.constant.POIUtils;
import com.itheIma.entity.Result;
import com.itheIma.pojo.OrderSetting;
import com.itheIma.service.OrderSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/27 18:11
 * @Creed 这一页的代码我看不懂
 **/

/**
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 读取文件
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile){
        try{
            List<String[]> list= POIUtils.readExcel(excelFile);
            if(list != null && list.size() > 0){
                List<OrderSetting> orderSettingList=new ArrayList<>();
                for(String[] str:list){
                    String orderDate=str[0];
                    String number=str[1];
                    OrderSetting orderSetting=new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                    orderSettingList.add(orderSetting);
                }
                orderSettingService.add(orderSettingList);
            }




        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);

            //获取预约设置数据成功

            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);

        }catch (Exception e){
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    /**
     * 根据指定日期修改可预约人数
     * @param orderSetting
     * @return
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //预约设置失败
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
