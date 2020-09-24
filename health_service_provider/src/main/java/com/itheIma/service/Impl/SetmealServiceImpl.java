package com.itheIma.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheIma.constant.RedisConstant;
import com.itheIma.dao.SetmealDao;
import com.itheIma.entity.PageResult;
import com.itheIma.pojo.Setmeal;
import com.itheIma.service.SetmealService;

import freemarker.template.Configuration;

import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/25 17:42
 * @Creed 这一页的代码我看不懂
 **/

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputpath ;




    /**
     * 添加套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {


        setmealDao.add(setmeal);


        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);

        }
        //图片存入redis
        try{
            savePic2Redis(setmeal.getImg());
        }catch (Exception e){
            e.printStackTrace();

        }




        //绑定完成生成动态界面
        generateMobileStaticHtml();
    }

    public  void generateMobileStaticHtml() {
        //查询所有的数据
        List<Setmeal> setmealList = setmealDao.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情页静态页面
        generateMobileSetmealDetailHtml(setmealList);

    }


    /**
     * 生成套餐列表静态页面
     *
     * @param setmealList
     */
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl", "m_setmeal.html", dataMap);
    }

    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", dataMap);
        }
    }

    private void generateHtml(String templateName, String htmlPageName, Map<String, Object> dataMap) {
        Configuration configuration=freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件

            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }



    /**
     * 图片存入redis
     *
     * @param pic
     */
    private void savePic2Redis(String pic) {

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }


    /**
     * 查询套餐
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(int id){
        return setmealDao.findByID(id);

    }

    /**
     * 绑定套餐和检查组的多对多关系
     *
     * @param id
     * @param checkgroupIds
     */
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {

        return setmealDao.findSetmealCount();
    }
}
