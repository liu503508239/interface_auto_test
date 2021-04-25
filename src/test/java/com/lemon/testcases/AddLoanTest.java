package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/15 23:44
 */
public class AddLoanTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成没有被注册的手机号码
        String borrowerPhone =  PhoneRandomUtil.getUnregisterPhone();
        String adminPhone =  PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowerPhone);
        Environment.envData.put("admin_phone",adminPhone);
        //读取用例0-4条
        List<ExcelPojo> list = readSpecifyExcelData(4,0,4);
        //循环遍历用例发起请求
        for (int i = 0; i < list.size(); i++) {
            ExcelPojo excelPojo = list.get(i);
            //替换用例数据
            excelPojo = caseReplace(excelPojo);
            //发起请求
            Response res = request(excelPojo,"加标模块");
            //判断是否提取返回数据
            if (excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }

        }
    }

    @Test(dataProvider = "getAddLoanDatas")
    public void testAddloan(ExcelPojo excelPojo){
        //替换
        excelPojo = caseReplace(excelPojo);
        //发起请求
        Response res = request(excelPojo,"加标模块");
        //断言
        assertResponse(excelPojo,res);

    }

    @DataProvider
    public Object[] getAddLoanDatas(){
        List <ExcelPojo> listDatas  = readSpecifyExcelData(4,4);
        return listDatas.toArray();
    }
    @AfterClass
    public void teardown(){

    }

}
