package com.lemon.testcases;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;
import java.util.List;


/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/16 23:28
 */
public class RegisterTest extends BaseTest {

    @BeforeClass
    public void setup() {
        //随机生成没有注册过的手机号码
        String phone1 = PhoneRandomUtil.getUnregisterPhone();
        String phone2 = PhoneRandomUtil.getUnregisterPhone();
        String phone3 = PhoneRandomUtil.getUnregisterPhone();

        //保存到变量中
        Environment.envData.put("phone2",phone2);
        Environment.envData.put("phone3",phone3);
        Environment.envData.put("phone1",phone1);
    }

    @Test(dataProvider = "getRegisterDatas")
    public void testRegister(ExcelPojo excelPojo) throws FileNotFoundException {





        //替换
        excelPojo = caseReplace(excelPojo);
        //发起注册请求
        Response res = request(excelPojo,"注册模块");

        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);


    }

    @DataProvider
    public Object[] getRegisterDatas() {
        List<ExcelPojo> listDatas = readSpecifyExcelData(1, 0);

        //把集合转换为一个一维数组

        return listDatas.toArray();

    }
    @AfterClass
    public void teardown(){


    }









}
