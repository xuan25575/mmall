package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

public interface ICartService {
    /**
     * 添加到购物车
     * @param userId
     * @param count
     * @param productId
     * @return ServerResponse<CartVo>
     */
    ServerResponse<CartVo> add(Integer userId, Integer count, Integer productId);

    /**
     * 修改购物车的产品的数量
     * @param userId
     * @param count
     * @param productId
     * @return  ServerResponse<CartVo>
     */
    ServerResponse<CartVo> update(Integer userId,Integer count,Integer productId);

    /**
     *  根据id删除 产品（可多个）  如删除三个 格式为 1,2,3，
     * @param productIds
     * @param userId
     * @return ServerResponse<CartVo>
     */
    ServerResponse<CartVo> deleteProduct(String productIds,Integer userId);

    /**
     *  查询购物车产品列表
     * @param userId
     * @return  ServerResponse<CartVo>
     */
    ServerResponse<CartVo> list(Integer userId);

    /**
     *  （全）单个选中 或 （全）单个不选中
     * @param userId
     * @param productId
     * @param checked
     * @return ServerResponse<CartVo>
     */
    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

    /**
     * 查看当前用户产品的数量
     * @param userId
     * @return ServerResponse<Integer>
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
