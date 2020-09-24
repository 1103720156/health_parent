package com.itheIma.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheIma.constant.MessageConstant;
import com.itheIma.constant.RedisConstant;
import com.itheIma.entity.PageResult;
import com.itheIma.entity.QueryPageBean;
import com.itheIma.entity.Result;
import com.itheIma.pojo.Setmeal;
import com.itheIma.utils.QiniuUtils;
import com.itheIma.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author 意风秋
 * @Date 2020/08/25 17:27
 * @Creed 这一页的代码我看不懂
 **/

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;


    /**
     * 图片上传
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        try{
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //图片上传成功

            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
            result.setData(fileName);
            //图片名称存入redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName );
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    /**
     * 增加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);

        }catch (Exception e){
            //新增套餐失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 查询套餐
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findpage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageR=null;
        pageR=setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());

        return  pageR;
    }
    /**
     * 编辑
     */

    /**
     * 删除
     */
}
