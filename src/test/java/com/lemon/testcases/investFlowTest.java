package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;

import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/14 0:49
 */
public class investFlowTest extends BaseTest {
    @BeforeClass
    public void setup(){

        //生成三个角色的随机手机号码（投资人+借款人+管理员）
        String borrowserPhone =  PhoneRandomUtil.getUnregisterPhone();
        String adminPhone =  PhoneRandomUtil.getUnregisterPhone();
        String investPhone =  PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone);
        Environment.envData.put("invest_phone",investPhone);



        //读取用例数据从第一条--第九条
        List<ExcelPojo> list = readSpecifyExcelData(5,0,9);

        for(int i =0;i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = caseReplace(excelPojo);
            Response res =  request(excelPojo,"业务流程模块");
            //判断是否要提取响应数据
            if (excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);

            }


        }


    }
    @Test
    public void testInvest(){

        List<ExcelPojo>list = readSpecifyExcelData(5,9);
        ExcelPojo excelPojo = list.get(0);
        //替换
        caseReplace(excelPojo);
        //发起投资请求
        Response res = request(excelPojo,"业务流程模块");
        //断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);



    }


    @AfterTest
    public void teardown(){

    }
}
