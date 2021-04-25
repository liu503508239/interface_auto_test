package com.lemon.testcases;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;

import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;



/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/5 21:50
 */
public class RechargeTest extends BaseTest {
    int memberId;
    String token;



    @BeforeClass
    public void setup() {
        //生成一个没有被注册的手机号
        String phone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone",phone);
        //前置条件
        //读取Excel里面的前两条数据

        List<ExcelPojo> listDatas = readSpecifyExcelData(3,0,2);
        Map <String,Object>requestHeaderMap01 = JSON.parseObject(listDatas.get(0).getRequestHeader());
        Map <String,Object>requestHeaderMap02 = JSON.parseObject(listDatas.get(1).getRequestHeader());
        //执行注册接口请求
        ExcelPojo excelPojo = listDatas.get(0);

        //注册请求
        excelPojo = caseReplace(excelPojo);
        Response resRegister = request(excelPojo,"充值模块");
        //获取【提取返回数据（extract）】
        //String extractStr = listDatas.get(0).getExtract();
        //提取接口返回对应的字段保存到环境变量中
        extractToEnvironment(excelPojo,resRegister);
        //参数替换，替换{{phone}}
        caseReplace(listDatas.get(1));
        //登录请求
        Response resLogin =request(listDatas.get(1),"充值模块");
        //得到【提取返回数据】这列
        extractToEnvironment(listDatas.get(1),resLogin);
    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        //用例执行之前替换{{member_id}}
        excelPojo= caseReplace(excelPojo);

        Response res = request(excelPojo,"充值模块");
        //断言
        assertResponse(excelPojo,res);
    }

    @DataProvider
    public Object[] getRechargeDatas() {
        List<ExcelPojo> listDatas = readSpecifyExcelData(3,2);
        return listDatas.toArray();


    }












}

