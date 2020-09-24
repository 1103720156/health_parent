package com.itheIma.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheIma.constant.MessageConstant;

import com.itheIma.entity.PageResult;
import com.itheIma.entity.QueryPageBean;
import com.itheIma.entity.Result;
import com.itheIma.pojo.CheckItem;
import com.itheIma.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



/**
 * 体检检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping(value="/add")
    public Result add(@RequestBody CheckItem checkItem){

        try {

            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询检查项
     * @param queryP
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryP){
        PageResult pageR=null;
        pageR=checkItemService.pageQuery(
                queryP.getCurrentPage(),
                queryP.getPageSize(),
                queryP.getQueryString());

        return  pageR;
    }

    /**
     * 删除检查项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping( value = "/delete")
    public Result delete(Integer id){
        try{
            checkItemService.delete(id);
        }
        catch(Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);

    }

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        try{
            CheckItem checkItem = checkItemService.findById(id);

             return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }catch (Exception e){
            e.printStackTrace();
        //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL); }
    }

    /**
     * 查询检查项
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping(value = "/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList = checkItemService.findAll();
        if(checkItemList != null && checkItemList.size() > 0){
            Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(checkItemList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}