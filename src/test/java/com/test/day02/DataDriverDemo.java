package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import java.util.List;
import java.util.Map;



import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/3/31 21:11
 */
public class DataDriverDemo {
    @Test(dataProvider =  "getLoginDatas02")
    public void testLogin(ExcelPojo excelPojo ) {

        //RestAssured
        //JSON小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl 全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //接口入参
        String inputparams=excelPojo.getInputParams();
        //接口地址
        String url = excelPojo.getUrl();
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //System.out.println(requestHeader);
        //把请求头转为map
        Map requestHeaderMap = (Map) JSON.parse(requestHeader);
        //期望的响应结果
        String expected = excelPojo.getExpected();
        //把响应结果转成map
        Map<String,Object> expectedMap = (Map) JSON.parse(expected);

        Response res =
                given().

                        body(inputparams).
                        headers(requestHeaderMap).
                when().
                        post(url).
                then().
                        log().all().extract().response();
        ////1.响应结果断言
        //读取响应map里面的每一个key
        //作业：完成响应断言
        //思路：
        //1.循环变量响应map，取到里面每一个key ,（实际上就是我们设计的JSONpath表达式）
        //2.通过res.jsonpath.get(key)取到实际结果值，再个期望的结果对比（可以对应的value值）
        //System.out.println(expectedMap);

        for(String key:expectedMap.keySet()){
            //获取map里的key，
            System.out.println(key);
            //获取map里的值，期望结果
            Object expectedValue = expectedMap.get(key);
            //获取res接口返回的实际结果
            Object  actualValue = res.jsonPath().get(key);
            Assert.assertEquals(actualValue,expectedValue);
        }








    }
    //@DataProvider
    //public Object[][] getLoginDatas(){
    //    Object[][] datas = {{"13565965806","12345678"},
    //            {"1356596580","12345678"},
    //            {"135659658061","12345678"}};
    //        return datas;
    //}

    @DataProvider
    public Object[] getLoginDatas02(){
        //读取Excel
        File file= new File("C:\\Users\\刘勇\\Desktop\\api_testcases_futureloan_practice01.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        List<ExcelPojo> listDatas =  ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        //把集合转换为一个一维数组
        return  listDatas.toArray();


    }

    public static void main(String[] args) {
        //读取Excel
        File file= new File("C:\\Users\\刘勇\\Desktop\\api_testcases_futureloan_practice01.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        List<ExcelPojo> listDatas= ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        //把集合转换为一个一维数组
        Object[] s =listDatas.toArray();
        for (Object o : s) {
            System.out.println(o);
        }





        }

}
