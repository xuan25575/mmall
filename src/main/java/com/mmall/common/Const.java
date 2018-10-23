package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER ="currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME ="username";


    public interface  ProductListOrderBy{
        Set<String> PRICE_DESC_ASC = Sets.newHashSet("price_desc","price_asc");
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
}
