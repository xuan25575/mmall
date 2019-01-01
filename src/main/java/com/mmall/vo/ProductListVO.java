package com.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductListVO {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;
    private Integer status;

    private String imageHost;

}