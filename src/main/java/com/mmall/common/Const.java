package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER ="currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME ="username";
    public static final String TOKEN_PREFIX = "token_";


    public interface RedisCacheExtime{
        int REDIS_SESSION_EXTIME = 60 * 30;//30分钟
    }
    public interface  ProductListOrderBy{
        Set<String> PRICE_DESC_ASC = Sets.newHashSet("price_desc","price_asc");
    }

    public interface  Cart{
        int CHECKED = 1;  //购物车选中状态
        int UN_CHECKED = 0;  //购物车未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";


    }

    public interface Role{
        int ROLE_CUSTOMER =  0; //普通用户
        int ROLE_ADMIN = 1;   //管理员
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        Integer code;
        String value;
        ProductStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }
        public Integer getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }
    }


    public interface AlipayCallback{
          String  TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
          String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

          String  RESPONSE_SUCCESS="RESPONSE_SUCCESS";
          String  RESPONSE_FAILED="RESPONSE_FAILED";
    }

    public enum OrderStatusEnum{
        CANCELED("已取消",0),
        NO_PAY("未付款",10),
        PAID("支付成功",20),
        SHIPPED("已发货",40),
        SUCCESS("交易成功",50);

        private String value;
        private Integer code;
        OrderStatusEnum(String value, Integer code) {
            this.value = value;
            this.code = code;
        }
        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有该类型的枚举");
        }

        public String getValue() {
            return value;
        }
        public Integer getCode() {
            return code;
        }
    }

   public enum PayPlatformEnum{
        ALIPAY("支付宝",1),;
       private String value;
       private Integer code;
       PayPlatformEnum(String value, Integer code) {
           this.value = value;
           this.code = code;
       }

       public String getValue() {
           return value;
       }

       public Integer getCode() {
           return code;
       }
   }


   public enum PaymentTypeEnum{
       ONLINE_PAY("在线支付",1);

       private String value;
       private Integer code;
       PaymentTypeEnum(String value, Integer code) {
           this.value = value;
           this.code = code;
       }

       public static PaymentTypeEnum codeOf(int code){
           for(PaymentTypeEnum paymentTypeEnum : values()){
               if(paymentTypeEnum.getCode() == code){
                   return paymentTypeEnum;
               }
           }
           throw new RuntimeException("没有该类型的枚举");
       }

       public String getValue() {
           return value;
       }

       public Integer getCode() {
           return code;
       }
   }

    public interface  REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";//关闭订单的分布式锁
    }

}
