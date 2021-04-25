package com.test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.annotations.TestInstance;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/3/29 22:02
 */
public class HomeWork {
    /**
     * @description: get请求
     * @param
     * @return: void
     * @author: liuYong
     * @time: 2021/3/29 22:05
     */
    @Test
    public void getDemo01() {
        given().
                //设置请求：请求头，请求体
                queryParam("mobilephone", "1356596805").
                queryParam("pwd", "123456").
        when().
                get("http://www.httpbin.org/get").
        then().
                log().body();
    }
    /**
     * @description: post 表单传参请求
     * @param
     * @return: void
     * @author: liuYong
     * @time: 2021/3/29 22:08
     */
    @Test
    public void postDemo01(){
        given().
                //设置请求：请求头，请求体
                queryParam("mobilephone", "1356596805").
                queryParam("pwd", "123456").
                contentType("application/x-www-form-urlencoded").
        when().
                post("http://www.httpbin.org/get").
        then().
                log().body();
    }
    /**
     * @description: post json格式传参请求
     * @param
     * @return: void
     * @author: liuYong
     * @time: 2021/3/29 22:09
     */
    @Test
    public void postDemo02() {
        String jsonData = "{\"mobile_phone\":\"13465965805\",\"pwd\":\"1234568\"}";
        given().
                body(jsonData).
                contentType("application/json").
        when().
                post("http://www.httpbin.org/post").
        then().log().all();
    }
    /**
     * @description: post html参数传递请求
     * @param
     * @return: void
     * @author: liuYong
     * @time: 2021/3/29 22:10
     */
    @Test
    public void postDemo03() {
        String xmlData = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "\t<meta charset=\"UTF-8\">\n" +
                "\t<title>Document</title>\n" +
                "\n" +
                "\t\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<select id =\"ss\">\n" +
                "\t\t<option>数学</option>\n" +
                "\t\t<option>语文</option>\n" +
                "\t\t<option>英语</option>\n" +
                "\n" +
                "\t</select>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        given().
                body(xmlData).
                contentType("application/xml").
        when().
                post("http://www.httpbin.org/post").
        then().log().all();
    }
    /**
     * @description: 传递文件
     * @param
     * @return: void
     * @author: liuYong
     * @time: 2021/3/29 22:11
     */
    @Test
    public void postDemo04() {
        given().
                multiPart(new File("D:\\安装参数.txt")).

                when().
                post("http://www.httpbin.org/post").
                then().log().all();


    }
    static String mobile_phone = "";
    static String pwd ="";
    static int  type =0;
    @Test
    public void testRegister(){

        String json = "{\"mobile_phone\":\"13915965804\",\"pwd\":\"12345678\",\"type\":\"0\",\n" +
                "\"reg_name\":\"管理员1号\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v1").
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().
                        log().all().extract().response();

        ////1.注册
        //// 1.1管理员账户

        ////获取管理员的手机号
        //String admin_mobile_phone = res.jsonPath().get("data.mobile_phone");
        ////1.2 投资人账户
        //String json01 = "{\"mobile_phone\":\"13915965805\",\"pwd\":\"12345678\",\"type\":\"1\",\n" +
        //        "\"reg_name\":\"投资人\"}";
        //Response res1 =
        //        given().
        //                body(json01).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v1").
        //                when().
        //                post("http://api.lemonban.com/futureloan/member/register").
        //                then().
        //                log().all().extract().response();
        ////获取投资人账户
        //String investor_mobile_phone = res1.jsonPath().get("data.mobile_phone");
        //
        ////1.3 借款人账户
        //String json02 = "{\"mobile_phone\":\"13915965807\",\"pwd\":\"12345678\",\"type\":\"0\",\n" +
        //        "\"reg_name\":\"借款人\"}";
        //Response res2 =
        //        given().
        //                body(json02).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v1").
        //                when().
        //                post("http://api.lemonban.com/futureloan/member/register").
        //                then().
        //                log().all().extract().response();
        ////获取借款人账户
        //String borrower_mobile_phone = res2.jsonPath().get("data.mobile_phone");
        //System.out.println("账户创建完毕");
        ////2.创建项目
        ////2.1借款人登录
        //String json03 = "{\"mobile_phone\":"+borrower_mobile_phone+",\"pwd\":\"12345678\"}";
        //Response res3 =
        //        given().
        //                body(json03).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v2").
        //
        //                when().
        //                post("http://api.lemonban.com/futureloan/member/login").
        //                then().
        //                log().all().extract().response();
        ////获取token
        //String token1 = res3.jsonPath().get("data.token_info.token");
        ////获取借款人用户id
        //int borrower_id= res3.jsonPath().get("data.id");
        ////2.2创建项目
        //String json04 = "{\"member_id\":"+borrower_id+",\n" +
        //        "\"title\":\"买车的\",\n" +
        //        "\"amount\":500000.00,\n" +
        //        "\"loan_rate\":\"20.0\",\n" +
        //        "\"loan_term\":6,\n" +
        //        "\"loan_date_type\":1,\n" +
        //        "\"bidding_days\":5}";
        //Response res4 =
        //        given().
        //                body(json04).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v2").
        //                header("Authorization","Bearer "+token1).
        //                when().
        //                post("http://api.lemonban.com/futureloan/loan/add").
        //                then().
        //                log().all().extract().response();
        ////获取项目ID
        //int loan_id = res4.jsonPath().get("data.id");
        //System.out.println("项目创建完毕");
        ////3.审核项目
        ////3.1管理员登录

        ////获取token
        //String token2 = res5.jsonPath().get("data.token_info.token");
        ////3.2审核项目
        //String json06 = "{\"loan_id\":"+loan_id+",\n" +
        //        "\"approved_or_not\":true}";
        //
        //        given().
        //                body(json06).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v2").
        //                header("Authorization","Bearer "+token2).
        //                when().
        //                patch("http://api.lemonban.com/futureloan/loan/audit").
        //                then().
        //                log().all().extract().response();
        //
        //System.out.println("项目审核完毕");
        ////4.投资
        ////4.1投资人登录
        //String json07 = "{\"mobile_phone\":"+investor_mobile_phone+",\"pwd\":\"12345678\"}";
        //Response res7 =
        //        given().
        //                body(json07).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v2").
        //
        //                when().
        //                post("http://api.lemonban.com/futureloan/member/login").
        //                then().
        //                log().all().extract().response();
        ////获取token
        //String token3 = res7.jsonPath().get("data.token_info.token");
        ////获取投资人ID
        //int memberid = res7.jsonPath().get("data.id");
        ////4.2充值

        ////4.3投资
        //String json09 = "{\"member_id\":"+memberid+",\n" +
        //        "\"loan_id\":"+loan_id+",\n" +
        //        "\"amount\":50000.00}";
        //
        //        given().
        //                body(json09).
        //                header("Content-Type","application/json").
        //                header("X-Lemonban-Media-Type","lemonban.v2").
        //                header("Authorization","Bearer "+token3).
        //                when().
        //                post("http://api.lemonban.com/futureloan/member/invest").
        //                then().
        //                log().all().extract().response();
        //System.out.println("投资成功");





    }
    @Test
    public void testLogin(){
        //String json05 = "{\"mobile_phone\":"+admin_mobile_phone+",\"pwd\":\"12345678\"}";
    //    Response res5 =
    //            given().
    //                    body(json05).
    //                    header("Content-Type","application/json").
    //                    header("X-Lemonban-Media-Type","lemonban.v2").
    //
    //                    when().
    //                    post("http://api.lemonban.com/futureloan/member/login").
    //                    then().
    //                    log().all().extract().response();
    //}
    //@Test
    //public void testRecharge(){
    //    String json08 = "{\"member_id\":"+memberid+",\"amount\":100000.00}";
    //
    //            given().
    //                    body(json08).
    //                    header("Content-Type","application/json").
    //                    header("X-Lemonban-Media-Type","lemonban.v2").
    //                    header("Authorization","Bearer "+token3).
    //                    when().
    //                    post("http://api.lemonban.com/futureloan/loan/audit").
    //                    then().
    //                    log().all().extract().response();
    }

}
