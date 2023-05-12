package com.wpl.volunteer.util;

import java.util.UUID;

public class GenerateUtil {

    public static void main(String[] args) {
        System.out.println(getVolunteerNumber("133245198502346763"));
    }

    public static String getVolunteerNumber(String idNumber){
        // 1.获取身份证号码前六位 6
        String prefix_idNumber = idNumber.substring(0,5);
        // 2.标识业务代码或机器代码（可变参数）3
        String machineId = "001";
        // 2.生成uuid的hashCode值 9
        int hashCode = UUID.randomUUID().toString().hashCode();
        // 3.可能为负数
        if(hashCode < 0){
            hashCode = -hashCode;
        }
        // 4.算法处理: 0-代表前面补充0; 9-代表长度为9; d-代表参数为正数型
        String value = prefix_idNumber + machineId + String.format("%09d", hashCode);

        return value;
    }

    private static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code) + 1);
        return result;
    }
}
