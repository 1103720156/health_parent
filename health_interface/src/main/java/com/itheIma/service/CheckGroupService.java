package com.itheIma.service;

import com.itheIma.entity.PageResult;
import com.itheIma.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/23 13:05
 * @Creed 这一页的代码我看不懂
 **/
public interface CheckGroupService {
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);


    void delete(Integer checkGroupId);

    List<CheckGroup> findAll();
}
