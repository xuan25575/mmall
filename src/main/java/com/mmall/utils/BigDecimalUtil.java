package com.mmall.utils;

import java.math.BigDecimal;

// 涉及金钱问题 用与处理double精度问题。
public class BigDecimalUtil {

    public BigDecimalUtil() {
    }
    // 注意： 原则是使用BigDecimal并且一定要用String来构造。 String不会有精度问题。（BigDecimal 是不可变的、任意精度的有符号十进制数）
    public static BigDecimal add(Double v1 ,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2);
    }
    public static BigDecimal sub(Double v1 ,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2);
    }
    public static BigDecimal mul(Double v1 ,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2);
    }
    public static BigDecimal div(Double v1 ,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);//四舍五入，保留两位小数
    }

}
