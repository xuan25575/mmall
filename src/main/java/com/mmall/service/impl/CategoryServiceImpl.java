package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

   // Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;


    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId == null && StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加参数有误");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);//品类是否可用
        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }


    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        //参数校验
        if(categoryId == null && StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新参数有误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        //成功返回结果
        if(rowCount>0){
            return ServerResponse.createBySuccessMessage("更新品类名称成功");
        }
        //失败返回结果
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }


     public ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId){
         List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
         if(org.apache.commons.collections.CollectionUtils.isEmpty(categoryList)){
             log.error("没有找到当前分类的子集合");
         }
         return ServerResponse.createBySuccess(categoryList);
     }

    /**
     * 递归查询本节点的id及子节点的id
     * @param categoryId
     * @return
     */
     public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
         Set<Category> categorySet  = Sets.newHashSet();
         findChildrenCategory(categorySet,categoryId);
         //guava 对集合的封装。
         List<Integer> categoryIdList = Lists.newArrayList();
         if(categoryId != null){
             for (Category categoryItem:categorySet) {
                 categoryIdList.add(categoryItem.getId());
             }
         }
         return ServerResponse.createBySuccess(categoryIdList);
     }

     private Set<Category> findChildrenCategory(Set<Category> categorySet, Integer categoryId){
         Category category = categoryMapper.selectByPrimaryKey(categoryId);
         if(category != null){
             categorySet.add(category);
         }
         //查找子节点，递归算法一定要有一个结束条件。  mybatis 返回的集合不用做非空判断 返回空的list
         List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
         for (Category categoryItem : categoryList){  // 亲测 回空的list不会进来遍历。这就是递归结束条件。 学到for循环也能做递归结束条件。
             findChildrenCategory(categorySet,categoryItem.getId());
         }
         return categorySet;
     }
}
