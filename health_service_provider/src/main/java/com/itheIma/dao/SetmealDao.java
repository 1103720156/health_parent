package com.itheIma.dao;

import com.github.pagehelper.Page;
import com.itheIma.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/25 17:47
 * @Creed 这一页的代码我看不懂
 **/
public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map map);

    Page<Setmeal> selectByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findByID(int id);
    List<Map<String,Object>> findSetmealCount();
}
