package com.lemon.common;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.lemon.data.Constants;

import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/7 20:53
 */
public class BaseTest {

    @BeforeTest
    public void GlobalSetup() throws FileNotFoundException {
        //JSON小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl 全局配置
        RestAssured.baseURI = Constants.BASE_URL;
        //日志全局重定向到本地文件中
        //File file = new File(System.getProperty("user.dir")+"\\log");
        //if (!file.exists()){
        //    //创建文件夹
        //    file.mkdir();
        //}

        //PrintStream fileOutPutStream = new PrintStream(new File("log/test_all.log"));
        //RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new ResponseLoggingFilter(fileOutPutStream));



    }

    /**
     * @description: 对get、post、patch、put。。。做了二次封装
     * @param excelPojo Excel每行数据对应的对象
     * @return: io.restassured.response.Response
     * @author: liuYong
     * @time: 2021/4/7 20:22
     */
    public Response request(ExcelPojo excelPojo ,String interfaceModuleName) {
        //为每一个请求单独的做日志保存
        //如果指定输出到文件的话，那么设置重定向输出到文件
        String logFilePath;
        if (Constants.LOG_TO_FILE) {
            File dirPath = new File(System.getProperty("user.dir") + "\\log\\" +interfaceModuleName);
            if (!dirPath.exists()) {
                //创建目录层级log/接口模块名
                dirPath.mkdirs();

            }

            logFilePath = dirPath+"\\test"+ excelPojo.getCaseId() + ".log";
            PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        //接口请求地址
        String url = excelPojo.getUrl();
        //请求方法
        String method = excelPojo.getMethod();
        //请求头
        String headers = excelPojo.getRequestHeader();
        //请求头转成map
        Map<String, Object> headersMap = JSONObject.parseObject(headers, Map.class);
        //请求参数
        String params = excelPojo.getInputParams();
        Response res = null;
        //对get、post、patch、put做封装
        if ("get".equalsIgnoreCase(method)) {
            res = given().log().all().headers(headersMap).when().get(url).then().log().all().extract().response();
        } else if ("post".equalsIgnoreCase(method)) {
            res = given().log().all().headers(headersMap).body(params).when().post(url).then().log().all().extract().response();

        } else if ("patch".equalsIgnoreCase(method)) {
            res = given().log().all().headers(headersMap).body(params).when().patch(url).then().log().all().extract().response();
        }
        //日志添加到allure
        if (Constants.LOG_TO_FILE){
        try {
            Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }
        return res;
    }
    /**
     * @description: 对响应结果断言
     * @param excelPojo 用例数据实体类对象
     * @param res 接口响应
     * @return: void
     * @author: liuYong
     * @time: 2021/4/16 23:00
     */
    public void assertResponse(ExcelPojo excelPojo,Response res){
        //对期望值判空
        if (excelPojo.getExpected()!= null){
            Map<String,Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected());
            for (String key:expectedMap.keySet()){
                //期望结果,获取map里面的value
                Object expectedValue = expectedMap.get(key);
                //实际结果，获取接口返回的实际结果（JSONpath表达式）
                Object actualValue = res.jsonPath().get(key);

                Assert.assertEquals(actualValue,expectedValue);
            }
        }
    }
    /**
     * @description: 数据库断言
     * @param excelPojo
     * @return: void
     * @author: liuYong
     * @time: 2021/4/19 22:54
     */
    public void assertSQL(ExcelPojo excelPojo){
        String dbAssert = excelPojo.getDbAssert();
        if (dbAssert !=null){
            Map<String,Object> map =JSONObject.parseObject(dbAssert, Map.class);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                //key其实就是我们之心的SQL语句
                //value就是数据库断言的期望值
                //期望结果
                Object expectedValue = map.get(key);
                if (expectedValue instanceof BigDecimal) {
                    Object actualValue = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actualValue, expectedValue);
                } else if (expectedValue instanceof Integer) {

                    //此时从excel里面读取到的是integer类型
                    //从数据库里面拿到是long类型
                    Long expectedValue2 = ((Integer) expectedValue).longValue();
                    Object actualValue = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actualValue, expectedValue2);


                }



            }
        }
    }




    /**
     * @description: 将对应的接口返回字段提取到环境变量中
     * @param excelPojo 用例数据对象
     * @param res response对象
     * @return: void
     * @author: liuYong
     * @time: 2021/4/12 22:29
     */
    public void extractToEnvironment(ExcelPojo excelPojo,Response res){
        Map<String,Object>extractMap = JSONObject.parseObject(excelPojo.getExtract(),Map.class);
        //循环遍历extractmap
        for (String key: extractMap.keySet()){
            Object path = extractMap.get(key);
            //根据【提取返回数据】里面的路径表达式去提取实际接口对应返回字段的值
            Object value = res.jsonPath().get(path.toString());
            //存到环境变量中
            Environment.envData.put(key,value);
        }

    }
    /**
     * @description: 从环境变量中取得对应的值，进行过正则替换
     * @param orgStr 原始的字符串
     * @return: 替换后的字符串
     * @author: liuYong
     * @time: 2021/4/12 22:44
     */
    public String regexReplace(String orgStr){

        if (orgStr !=null) {
            //pattern 正则表达式匹配器
            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");

            //matcher去匹配哪一个原始字符串
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            while (matcher.find()) {
                //group（0）表示获取到整个匹配到的内容
                String outerStr = matcher.group(0);
                //group(1)表示获取{{}}包裹着的内容
                String innerStr = matcher.group(1);
                //从环境变量中取到实际的值member_id
                Object replaceStr = Environment.envData.get(innerStr);
                //replace
                result = result.replace(outerStr, replaceStr + "");

            }
            return result;
        }return orgStr;
    }
    /**
     * @description: 对用例参数进行替换（入参+请求头+期望结果+URL）
     * @param excelPojo
     * @return: com.lemon.pojo.ExcelPojo
     * @author: liuYong
     * @time: 2021/4/13 23:31
     */
    public ExcelPojo caseReplace(ExcelPojo excelPojo){

        //正则替换--参数输入
        String inputParams = regexReplace(excelPojo.getInputParams());
        excelPojo.setInputParams(inputParams);
        //正则替换--请求头
        String requestHeader = regexReplace(excelPojo.getRequestHeader());
        excelPojo.setRequestHeader(requestHeader);
        //正则替换--接口地址
        String url = regexReplace(excelPojo.getUrl());
        excelPojo.setUrl(url);
        //正则替换--期望的返回结果
        String expected = regexReplace(excelPojo.getExpected());
        excelPojo.setExpected(expected);
        //正则替换--数据库校验
        String dbAssert = regexReplace(excelPojo.getDbAssert());
        excelPojo.setDbAssert(dbAssert);

        return excelPojo;

    }
    /**
     * @description: 读取Excel指定sheet里面的所有数据
     * @param sheetNum sheet编号
     * @return: void
     * @author: liuYong
     * @time: 2021/4/5 19:28
     */
    public List<ExcelPojo> readExcelData(int sheetNum){
        File file= new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个sheet页
        importParams.setStartSheetIndex(sheetNum-1);
        //读取Excel
        return ExcelImportUtil.importExcel(file, ExcelPojo.class,importParams);


    }
    /**
     * @description: 读取指定行的Excel表格数据
     * @param sheetNum sheet编号（从1开始）
     * @param startRow 读取开始行（默认从0开始）
     * @param readRow 读取多少行
     * @return: java.util.List<com.test.day02.ExcelPojo>
     * @author: liuYong
     * @time: 2021/4/7 21:03
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow, int readRow){
        File file= new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个sheet页
        importParams.setStartSheetIndex(sheetNum-1);
        ///设置读取的起始行
        importParams.setStartRows(startRow);
        //设置读取的行数
        importParams.setReadRows(readRow);
        //读取Excel
        return  ExcelImportUtil.importExcel(file, ExcelPojo.class,importParams);


    }
    /**
     * @description: 读取指定行开始的Excel表格数据
     * @param sheetNum sheet编号（从1开始）
     * @param startRow 读取开始行（默认从0开始）
     * @return: java.util.List<com.test.day02.ExcelPojo>
     * @author: liuYong
     * @time: 2021/4/7 21:03
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow){
        File file= new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个sheet页
        importParams.setStartSheetIndex(sheetNum-1);
        ///设置读取的起始行
        importParams.setStartRows(startRow);

        //读取Excel
        return ExcelImportUtil.importExcel(file, ExcelPojo.class,importParams);


    }
}
