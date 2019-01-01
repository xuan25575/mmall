package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.utils.CookieUtil;
import com.mmall.utils.JsonUtil;
import com.mmall.utils.PropertiesUtil;
import com.mmall.utils.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do")
    @ResponseBody
    public ServerResponse saveProduct(HttpServletRequest httpServletRequest,Product product){
       return  iProductService.saveOrUpdateProduct(product);
    }

    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest httpServletRequest,Integer productId,Integer status){
        return iProductService.updateSaleStatus(productId,status);
    }

    @RequestMapping(value = "get_detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest httpServletRequest,Integer productId){
          return  iProductService.manageProductDetail(productId);
    }

    @RequestMapping(value = "get_list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")  Integer pageSize){
        return iProductService.getProductList(pageNum,pageSize);
    }

    @RequestMapping(value = "product_search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest httpServletRequest, String productName,Integer productId,
          @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")  Integer pageSize){
        return iProductService.productSearch(productName,productId,pageNum,pageSize);
    }

    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping(value = "richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }

}
