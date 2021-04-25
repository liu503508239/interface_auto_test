package com.test.day01;

import groovy.transform.ASTTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/3/29 20:02
 */
public class RestAssuredDemo {
    @Test
    public void firstGetRequest() {
        given().
                //设置请求：请求头，请求体
                        when().
                get("https://www.baidu.com").
                then().log().body();

    }

    @Test
    public void getDemo01() {
        given().
                //设置请求：请求头，请求体
                        queryParam("mobilephone", "13323234545").
                queryParam("pwd", "123456").
                when().
                get("http://www.httpbin.org/get").
                then().
                log().body();
    }

    @Test
    public void postDemo01() {

        given().
                formParam("mobilephone", "13323234545").
                formParam("pwd", "1234567").
                contentType("application/x-www-form-urlencoded").
        when().
                post("http://www.httpbin.org/post").
        then().log().all();
    }

    @Test
    public void postDemo02() {
        String jsonData = "{\"mobile_phone\":\"nihao\",\"pwd\":\"1234567\"}";
        given().
                body(jsonData).
                contentType("application/json").
                when().
                post("http://www.httpbin.org/post").
                then().log().all();
    }

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

    @Test
    public void postDemo04() {
        given().
                multiPart(new File("D:\\安装参数.txt")).

                when().
                post("http://www.httpbin.org/post").
                then().log().all();


    }
}
