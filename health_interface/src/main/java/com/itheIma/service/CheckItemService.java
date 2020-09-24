package com.itheIma.service;

import com.itheIma.entity.PageResult;
import com.itheIma.pojo.CheckItem;

import java.util.List;

/**
 * @Author 意风秋
 * @Date 2020/08/20 17:34
 * @Creed 这一页的代码我看不懂
 **/
public interface CheckItemService {
    public void add(CheckItem checkItem);
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
    public void delete(Integer id);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    public List<CheckItem> findAll();
}
