package com.itheIma.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheIma.constant.MessageConstant;
import com.itheIma.entity.Result;

import com.itheIma.service.MemberService;
import com.itheIma.service.ReportService;
import com.itheIma.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 意风秋
 * @Date 2020/09/22 23:55
 **/

/**
 * 统计管理
 * @author Administrator
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    /**
     * 去年会员人数报表
     *
     * @return
     */
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        //获得当前日期之前12个月的日期,这里是获取前两年取一年的数据，方便调试
        calendar.add(Calendar.MONTH, -24);
        Map<String, Object> map = new HashMap<>();
        try {
            List<String> months = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                months.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }


            map.put("months", months);

            //查询每个月的人数
            List<Integer> memberCount = memberService.findMemberCountByMonth(months);
            map.put("memberCount", memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);

        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }


//    {
//        "data":{
//        "setmealNames":["套餐1", "套餐2", "套餐3"],
//        "setmealCount":[
//                {"name":"套餐1", "value":10 },
//                {"name":"套餐2", "value":30},
//                {"name":"套餐3", "value":25}
//        ]
//    },
//        "flag":true,
//            "message":"获取套餐统计数据成功"
//    }

    /**
     * 套餐统计
     *
     * @return
     */
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();

            List<String> setmealNames = new ArrayList<>();

            for (Map<String, Object> m : setmealCount) {
                String name = (String) m.get("name");
                setmealNames.add(name);
            }

            map.put("setmealNames", setmealNames);


            map.put("setmealCount", setmealCount);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL, map);
        }

    }



//        "data":{
//                "todayVisitsNumber":0,
//                "reportDate":"2019-04-25",
//                "todayNewMember":0,
//                "thisWeekVisitsNumber":0,
//                "thisMonthNewMember":2,
//                "thisWeekNewMember":0,
//                "totalMember":10,
//                "thisMonthOrderNumber":2,
//                "thisMonthVisitsNumber":0,
//                "todayOrderNumber":0,
//                "thisWeekOrderNumber":0,
//                "hotSetmeal":[
//                        { "proportion":0.4545, "name":"粉红珍爱(女)升级TM12项筛查体检套 餐", "setmeal_count":5 },
//                        { "proportion":0.1818, "name":"阳光爸妈升级肿瘤12项筛查体检套 餐", "setmeal_count":2 },
//        { "proportion":0.1818, "name":"珍爱高端升级肿瘤12项筛查", "setmeal_count":2 },
//        { "proportion":0.0909, "name":"孕前检查套餐", "setmeal_count":1 }
//        ],
//    },
//     "flag":true,
//     "message":"获取运营统计数据成功"


    /**
     * 运营数据统计
     *
     * @return
     */
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {

        try{
            Map<String,Object> map=reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }


    /**
     * 导出报表
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReport();
            //取出返回结果数据，准备将报表数据写入到Excel文件中 S
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //获得Excel模板文件绝对路径
            String temlateRealPath =
                    request.getSession().getServletContext().getRealPath("template") +
                            File.separator + "report_template.xlsx";//动态识别系统window\,linux/
            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);
            //日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            //新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);
            //总会员数
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            //本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);
            //本月新增会员数
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            //今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);
            //今日到诊数
            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            //本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);
            //本周到诊数
            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            //本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);
            //本月到诊数
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                //热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                //套餐名称
                row.getCell(5).setCellValue(setmeal_count);
                //预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());
                //占比
            }
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            //代表的是Excel文件类型
            response.setContentType("application/vnd.ms-excel");
            //指定以附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);


        }
    }


    /**
     * 导出运营数据到PDF文件并提供客户端下载
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> result = reportService.getBusinessReport();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //动态获取pdf模板文件绝对磁盘路径
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";

            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperPath,result,
                            new JRBeanCollectionDataSource(hotSetmeal));

            //创建输出流，用于从服务器写数据到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);

            out.flush();
            out.close();

            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
