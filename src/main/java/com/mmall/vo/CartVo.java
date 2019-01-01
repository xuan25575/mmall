package com.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    private boolean allChecked;//是否全部勾选
    private String imageHost;

}
