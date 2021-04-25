package com.test.day02;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/3/31 19:57
 */
public class AssertDemo {
    int memberId;
    String token;
    @Test
    public void testLogin(){
        //RestAssured 全局配置
        //JSON小数返回类型是BigDecimal
        RestAssured.config =RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl 全局配置
        RestAssured.baseURI="http://api.lemonban.com/futureloan";

        String json05 = "{\"mobile_phone\":\"13565965806\",\"pwd\":\"12345678\"}";
            Response res =
                    given().

                            body(json05).
                            header("Content-Type","application/json").
                            header("X-Lemonban-Media-Type","lemonban.v2").

                    when().
                            post("/member/login").
                    then().
                            log().all().extract().response();
        //1.响应结果断言
        //整数类型
        int code = res.jsonPath().get("code");
        Assert.assertEquals(code,0);
        //字符串
        String msg = res.jsonPath().get("msg");
        Assert.assertEquals(msg,"OK");
        //小数类型
        //注意：restassured里面如果返回json小数，那么其类型是float
        //丢失精度问题解决方案，声明restassured返回json的其类型是BigDecimal

        BigDecimal actual=res.jsonPath().get("data.leave_amount");
        BigDecimal expected = BigDecimal.valueOf(395555.55);
        Assert.assertEquals(actual,expected);
        //java.lang.AssertionError: expected [355555.55] but found [355555.55]
        //因为类型不匹配
        //2.数据库断言
        memberId = res.jsonPath().get("data.id");
        token = res.jsonPath().get("data.token_info.token");

    }
    @Test(dependsOnMethods = "testLogin")
    public void testRecharge(){
        //发起充值接口请求

        String json08 = "{\"member_id\":"+memberId+",\"amount\":10000.00}";
        Response res2=
                given().
                        body(json08).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Authorization","Bearer "+token).
                        when().
                        post("/member/recharge").
                        then().
                        log().all().extract().response();
        BigDecimal actual2 = res2.jsonPath().get("data.leave_amount");
        BigDecimal expected2 = BigDecimal.valueOf(415555.55);
        Assert.assertEquals(actual2,expected2);
    }
}
