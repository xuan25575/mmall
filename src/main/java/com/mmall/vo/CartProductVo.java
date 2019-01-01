package com.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartProductVo {
    //结合产品和购物车的一个抽象
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity; //购物车中产品数量
    private String productName;
    private String productSubTitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    private Integer productChecked; //此商品是否被勾选。

    private String limitQuantity; //限制数量的一个返回结果。
}
