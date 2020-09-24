package com.itheIma.dao;

import com.github.pagehelper.Page;
import com.itheIma.pojo.CheckItem;

import java.util.List;

/**
 * @Author 意风秋
 * @Date 2020/08/19 10:25
 * @Creed 这一页的代码我看不懂
 **/
public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<CheckItem> selectByCondition(String queryString);

   public void deleteById(Integer id);
    void delete(Integer id);

   public long findCountByCheckItemId(Integer id);

   public CheckItem findById(Integer id);

   public void edit(CheckItem checkItem);

   public List<CheckItem> findAll();
}
