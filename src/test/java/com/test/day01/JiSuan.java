package com.test.day01;

import org.apache.http.impl.execchain.TunnelRefusedException;

import java.util.Random;

/**
 * @description:
 * @author: liuYong
 * @time: 2021/4/23 8:44
 */
public class JiSuan {
    public static  void jisuan(int a ){

        if (!(99<a && a<1000)){
            System.out.println("a只限3位数！");
            return;
        }
        for (int i=1;i<10;i++){
            for (int j=0;j<10;j++){
                for (int k=0;k<10;k++){
                    int sum = j+k*10+i*100 ;
                    if (a == i+j+k+sum){
                        System.out.println(sum);
                        return;
                    }
                }
            }
        }
        System.out.println(a+"没有最小生成元");
    }

    public static void main(String[] args) {

            int i =1;
            if (true){
                i++;
                System.out.println(i);
                return;
            }else {
                i--;
            }
            System.out.println(i++);

    }

}
