package com.itheIma.jobs;

import com.itheIma.constant.RedisConstant;
import com.itheIma.utils.AliUtils;
import com.itheIma.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author 意风秋
 * @Date 2020/08/26 19:26
 * @Creed 这一页的代码我看不懂
 **/
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;


    /**
     *
     */
    public void clearImg() {

        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set != null) {
            for (String picName : set) {
                //删除七牛云服务器上的图片

                AliUtils.deleteFileFromAli(picName);
                //从Redis集合中删除图片名称
                jedisPool.getResource(). srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }

    }
}
