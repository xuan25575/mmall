package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            //todo 里面有shippingId吗？ 前台没有传值为啥有呢？
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新增地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新增地址失败");
    }

    public ServerResponse del(Integer userId,Integer shippingId){
        //防止横向越权
        int rowCount = shippingMapper.deleteByShippingIdUserId(shippingId,userId);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    public ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByUserId(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
        //防止横向越权
        Shipping shipping = shippingMapper.selectByUserIdShippingId(shippingId,userId);
        if(shipping != null){
            return ServerResponse.createBySuccess("查询地址成功" ,shipping);
        }
        return ServerResponse.createByErrorMessage("查询地址失败");
    }

    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
