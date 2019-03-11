package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.utils.DateUtil;
import com.mmall.utils.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse saveOrUpdateProduct(Product product){
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String [] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            //通过id判断是修改还是添加
            if(product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }else{
                int insert = productMapper.insert(product);
                if(insert >0){
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }
        }
         return ServerResponse.createByErrorMessage("更新或添加产品参数错误");
    }

    public ServerResponse<String> updateSaleStatus(Integer productId,Integer status){
        if(productId ==  null && status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product =new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount >0){
            return ServerResponse.createBySuccess("修改销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");

    }

     public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
         if(productId == null ){
             return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
         }
         Product product = productMapper.selectByPrimaryKey(productId);
         if(product == null ){
             return ServerResponse.createByErrorMessage("商品已经下架或者删除了");
         }
         ProductDetailVo productDetailVo = assembleProductDetailVo(product);
         return ServerResponse.createBySuccess(productDetailVo);
     }

     // 主要用来将pojo和VO转化， VO前端数据和控件的绑定操作。
     private ProductDetailVo assembleProductDetailVo(Product product){
         ProductDetailVo productDetailVo = new ProductDetailVo();
         productDetailVo.setId(product.getId());
         productDetailVo.setName(product.getName());
         productDetailVo.setCategoryId(product.getCategoryId());
         productDetailVo.setDetail(product.getDetail());
         productDetailVo.setMainImage(product.getMainImage());
         productDetailVo.setPrice(product.getPrice());
         productDetailVo.setStatus(product.getStatus());
         productDetailVo.setSubImages(product.getSubImages());
         productDetailVo.setStock(product.getStock());

         Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
         if(category == null){
             productDetailVo.setParentCategoryId(0);  //默认为根节点。
         }else{
             productDetailVo.setParentCategoryId(category.getParentId());
         }
         productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
         productDetailVo.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
         productDetailVo.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
         return  productDetailVo;
     }

     public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize){
         //startPage -- start
         //填充自己的sql查询逻辑
         //pageHelper 收尾(AOP 实现分页)
         PageHelper.startPage(pageNum,pageSize);
         List<Product> productList = productMapper.selectList();
         List <ProductListVO> productListVOList = Lists.newArrayList();
         for(Product productItem : productList){
             ProductListVO productListVO = assembleProductListVo(productItem);
             productListVOList.add(productListVO);
         }
         //结果集进行包装
         PageInfo result = new PageInfo(productList);
         //处理结果集之后的参数设置
         result.setList(productListVOList);
         return ServerResponse.createBySuccess(result);
     }

     private  ProductListVO assembleProductListVo(Product product){
         ProductListVO productListVO = new ProductListVO();
         productListVO.setId(product.getId());
         productListVO.setName(product.getName());
         productListVO.setCategoryId(product.getCategoryId());
         productListVO.setMainImage(product.getMainImage());
         productListVO.setPrice(product.getPrice());
         productListVO.setStatus(product.getStatus());
         productListVO.setSubtitle(product.getSubtitle());
         productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
         return productListVO;
     }

     public ServerResponse<PageInfo> productSearch(String productName,Integer productId,Integer pageNum, Integer pageSize) {
         PageHelper.startPage(pageNum,pageSize);
         if(StringUtils.isNotBlank(productName)){
             productName = new StringBuffer().append("%").append(productName).append("%").toString();
         }
         List<Product> productList = productMapper.selectListByproductNameAndProductId(productName, productId);
         List <ProductListVO> productListVOList = Lists.newArrayList();
         for(Product productItem : productList){
             ProductListVO productListVO = assembleProductListVo(productItem);
             productListVOList.add(productListVO);
         }
         PageInfo result = new PageInfo(productList);
         result.setList(productListVOList);
         return ServerResponse.createBySuccess(result);
     }

     public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
         if(productId == null ){
             return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
         }
         Product product = productMapper.selectByPrimaryKey(productId);
         if(product == null ){
             return ServerResponse.createByErrorMessage("商品已经下架或者删除了");
         }
         if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode() ){
             return ServerResponse.createByErrorMessage("商品已经下架或者删除了");
         }
         ProductDetailVo productDetailVo = assembleProductDetailVo(product);
         return ServerResponse.createBySuccess(productDetailVo);
     }


    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有关键字，没有该分类，这时候返回一个空的结果集。
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(categoryId).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuffer().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_DESC_ASC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectListByNameAndCategoryIds(StringUtils.isBlank(keyword)? null:keyword,
                categoryIdList.size() == 0 ? null :categoryIdList);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVO productListVO = assembleProductListVo(productItem);
            productListVOList.add(productListVO);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
