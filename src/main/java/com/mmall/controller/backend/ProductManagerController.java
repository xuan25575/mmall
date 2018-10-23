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
import com.mmall.utils.PropertiesUtil;
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
    public ServerResponse saveProduct(HttpSession session,Product product){
        System.out.println(product.getName());
         User user = (User)session.getAttribute(Const.CURRENT_USER);
         if(user == null){
             return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，请登录管理员");
         }
         if(iUserService.checkAdmin(user).isSuccess()){
            //做添加的逻辑
             return  iProductService.saveOrUpdateProduct(product);
         }else{
             return ServerResponse.createBySuccessMessage("么有操作权限");
         }
    }

    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，请登录管理员");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            //更新状态
            return  iProductService.updateSaleStatus(productId,status);
        }else{
            return ServerResponse.createBySuccessMessage("么有操作权限");
        }
    }

    @RequestMapping(value = "get_detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，请登录管理员");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            //获取商品详情
            return  iProductService.manageProductDetail(productId);
        }else{
            return ServerResponse.createBySuccessMessage("么有操作权限");
        }
    }

    @RequestMapping(value = "get_list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")  Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，请登录管理员");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            //获取商品列表
            return iProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createBySuccessMessage("么有操作权限");
        }
    }

    @RequestMapping(value = "product_search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName,Integer productId,
          @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")  Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，请登录管理员");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            //搜素逻辑
            return iProductService.productSearch(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.createBySuccessMessage("么有操作权限");
        }
    }

    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，请登录管理员");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map filemap = Maps.newHashMap();
            filemap.put("uri",targetFileName);
            filemap.put("url",url);
            return ServerResponse.createBySuccess(filemap);
        }else{
            return ServerResponse.createBySuccessMessage("么有操作权限");
        }
    }

    @RequestMapping(value = "richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success","false");
            resultMap.put("msg","用户没有登录,请登录管理员");
            return resultMap;
        }
        //富文本对于返回值有自己的要求，我们使用是simditor 所以要按照 simditor要求返回。
//        "success":"false"
//        "msg":"上传失败"
//        "file_path":url

        if(iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success","false");
                resultMap.put("msg","上传失败");
                return resultMap;
            }else{
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
                resultMap.put("success","true");
                resultMap.put("msg","上传成功");
                resultMap.put("file_path",url);
                //response要求 ，需要修改。
                response.setHeader("Access-Control-Allow-Headers","X-File-Name");
                return resultMap;
            }
        }else{
            resultMap.put("success","false");
            resultMap.put("msg","么有操作权限");
            return resultMap;
        }
    }

}
