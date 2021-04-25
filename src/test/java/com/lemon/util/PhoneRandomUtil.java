package com.lemon.util;

import org.apache.poi.hssf.record.PageBreakRecord;

import java.util.Random;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/15 0:35
 */
public class PhoneRandomUtil {
    public static void main(String []args){
        //思路一、先查询手机号字段，按照倒叙排列，取得最大的手机号+1
        //思路二、先去生成一个随机的手机号码，在通过数据库查询，如果有记录，再生成，否则说明手机号没有被注册

        System.out.println(getUnregisterPhone());


    }
    public static String getRadnomPhone(){
        Random random = new Random();
        //nextInt 随机生成一个整数，范围是从0到你的参数范围之内
        random.nextInt(10);
        String phonePrefix ="133";
        //生成8位随机的整数
        for (int i =0 ;i<8;i++){
            int num = random.nextInt(9);
            phonePrefix = phonePrefix+num;
        }
        //System.out.println(phonePrefix);
        return phonePrefix;
    }
    public static  String getUnregisterPhone(){
        String phone ="";
        while (true){
            phone = getRadnomPhone();
            //查询数据
            Object result = JDBCUtils.querySingleData("select count(*) from member where mobile_phone="+phone);

            if ((Long) result == 0){
                //符合需求
                break;
            }else{
                //表示已被注册，继续生成
                continue;
            }
        }
        return phone ;
    }




}
