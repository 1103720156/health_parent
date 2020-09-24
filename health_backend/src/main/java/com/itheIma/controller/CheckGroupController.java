package com.itheIma.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheIma.constant.MessageConstant;
import com.itheIma.entity.PageResult;
import com.itheIma.entity.QueryPageBean;
import com.itheIma.entity.Result;
import com.itheIma.pojo.CheckGroup;
import com.itheIma.pojo.CheckItem;
import com.itheIma.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 意风秋
 * @Date 2020/08/23 12:59
 * @Creed 这一页的代码我看不懂
 **/

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 增加检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try{
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 检查组分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString() );
        return pageResult;
    }


    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if(checkGroup != null){
            Result result = new Result(true,
                    MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
            return result;
        }return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }


    /**
     * 根据检查组合id查询对应的所有检查项id
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try{
            List<Integer> checkitemIds =
                    checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,
                    MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,
                    MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            return new Result(false,
                    MessageConstant.EDIT_CHECKGROUP_FAIL);
        }return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 删除检查组
     * @param checkGroupId
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer checkGroupId){
        try{

            checkGroupService.delete(checkGroupId);
        }catch (Exception e){
            return new Result(false,
                    MessageConstant.DELETE_CHECKGROUP_FAIL);

        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询数据
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> checkItemList = checkGroupService.findAll();
        if(checkItemList != null && checkItemList.size() > 0){
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkItemList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }
}
