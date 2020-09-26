package com.itheIma.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author 意风秋
 * @Date 2020/09/26 17:03
 **/
public class AliUtils {
    //阿里云API的外网域名
    private static String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID = "LTAI4GHCc4bkipKp6chDxdcx";
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET = "HfXylsd2334veVNQwVzZJBYWhkk47V";
    //阿里云API的bucket名称
    private static String BACKET_NAME = "health-bhq";
    //阿里云API的文件夹名称
    private static String FOLDER="health/";

    /**
     * 获取阿里云OSS客户端对象
     * @return ossClient
     */
    public static OSSClient getOSSClient(){
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 增加图片
     * @param bytes
     * @param fileName
     */
    public static void upload2Ali(byte[] bytes, String fileName){
        //初始化OSSClient
        OSSClient ossClient=AliUtils.getOSSClient();
        String str=null;
        try{
            //以输入流的形式上传文件
            InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(BACKET_NAME, FOLDER +
                    fileName,byteArrayInputStream);
            //解析结果
            str=putResult.getETag();
            System.out.println("唯一MD:"+str);

        }catch (Exception e){
            e.printStackTrace();
        }
        ossClient.shutdown();

    }

    /**
     * 删除文件
     * @param fileName
     */
    public static void deleteFileFromAli(String fileName){

        //初始化OSSClient
        OSSClient ossClient=AliUtils.getOSSClient();
        try{

            ossClient.deleteObject(BACKET_NAME,FOLDER+fileName);
            //解析结果

            System.out.println("删除图片成功："+fileName);

        }catch (Exception e){
            e.printStackTrace();
        }
        ossClient.shutdown();


    }

}
