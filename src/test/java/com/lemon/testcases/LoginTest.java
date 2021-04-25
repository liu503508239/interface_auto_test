package com.lemon.testcases;


import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Constants;

import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;


import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/4 18:18
 */
public class LoginTest extends BaseTest {

    @BeforeClass
    public void setup(){

        //生成一个没有被注册的手机号
        String phone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone",phone);

        //前置条件
        //读取Excel里面的第一条数据>执行>生成一条注册过的手机号
        List<ExcelPojo> listDatas = readSpecifyExcelData(2,0,1);
        ExcelPojo excelPojo = listDatas.get(0);
        //替换
        excelPojo = caseReplace(excelPojo);
        //执行注册接口请求
        Response res = request(excelPojo,"登录模块");
        //提取注册接口返回的手机号码保存到环境变量中
        extractToEnvironment(excelPojo,res);

    }

    @Test(dataProvider =  "getLoginDatas")
    public void testLogin(ExcelPojo excelPojo ) {
        //替换用例的数据
        excelPojo = caseReplace(excelPojo);
        //发起登录请求
        Response res = request(excelPojo,"登录模块");
        //断言
        Map<String,Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected());



        ////1.响应结果断言
        //读取响应map里面的每一个key
        //作业：完成响应断言
        //思路：
        //1.循环变量响应map，取到里面每一个key ,（实际上就是我们设计的JSONpath表达式）
        //2.通过res.jsonpath.get(key)取到实际结果值，再个期望的结果对比（可以对应的value值）
        //System.out.println(expectedMap);

       //断言
        assertResponse(excelPojo,res);
    }


    @DataProvider
    public Object[] getLoginDatas(){
        //读取Excel
        List<ExcelPojo> listDatas= readSpecifyExcelData(2,1);


        //把集合转换为一个一维数组
        return  listDatas.toArray();


    }









}
