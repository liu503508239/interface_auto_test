package com.lemon.util;

import com.lemon.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @description: 创建数据库连接
 * @author: liuYong
 * @time: 2021/4/14 23:15
 */
public class JDBCUtils {
    public static Connection getConnection(){
        //定义数据库连接
        //oracle:jdbc:oracle:thin:@localhost:1521:DBName
        //sqlServer:jdbc:microsoft:sqlserver://localhost:1433;DataBaseName=DBName
        //mysql: jdbc:mysql://localhost:3306/DBName
        String url = "jdbc:mysql://"+ Constants.DB_BASE_URI+"/"+Constants.DB_NAME+"?useUnicode=true&characterEncoding=utf-8";
        String user = Constants.DB_USERNAME;
        String password = Constants.DB_PWD;
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            e.printStackTrace();
        }return conn;
    }
    /**
     * @description: 关闭数据库连接
     * @param connection  数据库连接对象
     * @return: void
     * @author: liuYong
     * @time: 2021/4/16 22:52
     */
    public static void closeConnection(Connection connection){
        //判空
        if (connection!=null){
            //关闭数据库连接
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }




    }


    /**
     * @description: sql的更新操作（包括了增加+修改+删除）
     * @param sql 要执行的SQL语句
     * @return: void
     * @author: liuYong
     * @time: 2021/4/15 0:22
     */
    public static void update(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭数据库连接
            closeConnection(connection);
        }

    }
    /**
     * @description: 查询所有的结果集
     * @param sql 要执行的查询语句
     * @return: 返回的结果集
     * @author: liuYong
     * @time: 2021/4/15 0:26
     */
    public static List<Map<String, Object>> queryAll(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String,Object>> result = null;
        try {
            result = queryRunner.query(connection,sql,new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }
    /**
     * @description: 查询单条的数据
     * @param sql 要执行的查询语句
     * @return: 返回的结果集
     * @author: liuYong
     * @time: 2021/4/15 0:26
     */
    public static Object querySingleData(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Object result = null;
        try {
            result = queryRunner.query(connection,sql,new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }
    /**
     * @description: 查询结果集的第一条数据
     * @param sql 要执行的查询语句
     * @return: 返回的结果集
     * @author: liuYong
     * @time: 2021/4/15 0:26
     */
    public static Map<String, Object> queryOne(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Map<String,Object> result = null;
        try {
            result = queryRunner.query(connection,sql,new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }

}
