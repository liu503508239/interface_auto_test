package com.test.day01;

import io.restassured.response.Response;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/3/29 20:42
 */
public class GetResponse {
    @Test
    public void getResponseHeader(){
        Response response =
        given().
                when().
                    post("http://www.httpbin.org/post").
                then().
                    log().all().extract().response();
        System.out.println("接口的相应时间："+response.time());
        System.out.println(response.getHeader("Content-Type"));
    }

    @Test
    public void getResponseJson01(){
        String json = "{\"mobile_phone\":\"13565965806\",\"pwd\":\"12345678\"}";
        Response response =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v1").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().
                        log().all().extract().response();
        System.out.println((char[]) response.jsonPath().get("data.id"));
    }
    @Test
    public void getResponseJson02(){
        Response response =
                given().
                        when().
                        get("http://httpbin.org/json").
                        then().
                        log().all().extract().response();
        System.out.println((char[]) response.jsonPath().get("slideshow.slides.title"));
        List<String>list = response.jsonPath().getList("slideshow.slides.title");
        System.out.println(list.get(0));

    }
    @Test
    public void getResponseHtml(){
        Response response =
                given().
                        when().
                        get("http://www.baidu.com").
                        then().
                        log().all().extract().response();
        System.out.println((char[]) response.htmlPath().get("html.head.meta[0].@content"));
        System.out.println(response.htmlPath().getList("html.head.meta"));
    }
    @Test
    public void getResponseXml(){
        Response response =
                given().
                        when().
                        get("http://httpbin.org/xml").
                        then().
                        log().all().extract().response();
        System.out.println((char[]) response.xmlPath().get("slideshow.slide[1].title"));
        System.out.println((char[]) response.xmlPath().get("slideshow.slide[1].@type"));
    }

    @Test
    public void loginRecharge(){
        String json = "{\"mobile_phone\":\"13565965806\",\"pwd\":\"12345678\"}";
        Response response =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        //1.先来获取ID
        int memberId = response.jsonPath().get("data.id");
        System.out.println(memberId);
        //2.获取token
        String token = response.jsonPath().get("data.token_info.token");
        System.out.println(token);
        //发起充值
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":\"100000.00\"}";
        Response res =
                given().
                        body(jsonData).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Authorization","Bearer "+token).
                        when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                        then().
                        log().all().extract().response();
        System.out.println("当前可用余额："+res.jsonPath().get("data.leave_amount"));
    }
}
