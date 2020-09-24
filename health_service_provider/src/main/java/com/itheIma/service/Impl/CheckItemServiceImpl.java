package com.itheIma.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheIma.dao.CheckItemDao;
import com.itheIma.entity.PageResult;
import com.itheIma.pojo.CheckItem;
import com.itheIma.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author 意风秋
 * @Date 2020/08/19 10:22
 * @Creed 这一页的代码我看不懂
 **/

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;


    //新增

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }


    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        /*//判断关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            throw new RuntimeException("当前检查项被引用，不能删除");
        }*/
        checkItemDao.delete(id);//删除关系
        checkItemDao.deleteById(id);//删除检查项
    }


    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }


    @Override
    public CheckItem findById(Integer id) {
      return   checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll(){
        return checkItemDao.findAll();
    }

}
