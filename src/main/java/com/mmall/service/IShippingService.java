package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {
    /**
     * 新增地址
     * @param userId
     * @param shipping
     * @return ServerResponse
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return ServerResponse
     */
    ServerResponse del(Integer userId,Integer shippingId);

    /**
     *修改收货地址
     * @param userId
     * @param shipping
     * @return ServerResponse
     */
    ServerResponse update(Integer userId,Shipping shipping);

    /**
     *  查询单个收货地址
     * @param userId
     * @param shippingId
     * @return ServerResponse<Shipping>
     */
    ServerResponse<Shipping> select(Integer userId,Integer shippingId);

    /**
     * 分页查询收货地址
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return ServerResponse<PageInfo>
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
