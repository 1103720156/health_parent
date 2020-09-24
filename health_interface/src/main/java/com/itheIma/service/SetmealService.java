package com.itheIma.service;

import com.itheIma.entity.PageResult;
import com.itheIma.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/25 17:33
 * @Creed 这一页的代码我看不懂
 **/
public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal findById(int id);
    List<Map<String,Object>> findSetmealCount();
}
