package com.itheIma.dao;

import com.github.pagehelper.Page;
import com.itheIma.pojo.CheckGroup;
import com.itheIma.pojo.CheckItem;

import java.util.List;
import java.util.Map;

/**
 * @Author 意风秋
 * @Date 2020/08/23 13:07
 * @Creed 这一页的代码我看不懂
 **/
public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);
    public void setCheckGroupAndCheckItem(Map map);


    public Page<CheckItem> selectByCondition(String queryString);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    CheckGroup findById(Integer id);


    void deleteCheckGroup(Integer checkGroupId);

    List<CheckGroup> findAll();

}
