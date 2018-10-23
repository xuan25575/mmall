package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {
    /**
     *添加或者修改商品
     * @param product
     * @return ServerResponse
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 修改商品销售状态
     * @param productId
     * @param status
     * @return  ServerResponse<String>
     */
    ServerResponse<String> updateSaleStatus(Integer productId,Integer status);

    /**
     * 查看商品详细
     * @param productId
     * @return  ServerResponse<ProductDetailVo>
     */
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return ServerResponse<PageInfo>
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    /**
     * 根据产品名字和id查询列表 （搜索）
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return ServerResponse<PageInfo>
     */
    ServerResponse<PageInfo> productSearch(String productName,Integer productId,Integer pageNum, Integer pageSize);

    /**
     * 前台获取商品详情
     * @param productId
     * @return   ServerResponse<ProductDetailVo>
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     *  前台分页数据查询列表
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return ServerResponse<PageInfo>
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}
