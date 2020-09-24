package com.itheIma.service;

import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/09/23 19:12
 **/
public interface ReportService {
    /** 获得运营统计数据
     * Map数据格式：
     *  todayNewMember -> number
     *  totalMember -> number
     *  thisWeekNewMember -> number
     *  * thisMonthNewMember -> number
     *  todayOrderNumber -> number
     *  todayVisitsNumber -> number
     *  thisWeekOrderNumber -> number
     *  thisWeekVisitsNumber -> number
     *  thisMonthOrderNumber -> number
     *  thisMonthVisitsNumber -> number
     *  hotSetmeals -> List<Setmeal>
     */
    Map<String,Object> getBusinessReport() throws Exception;
}
