package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId" ,defaultValue="0") int parentId){
       User user = (User)session.getAttribute(Const.CURRENT_USER);
       if(user==null){
           return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户没有登录，需要登录");
       }
       if(iUserService.checkAdmin(user).isSuccess()){
            // 是管理员
            // 做添加逻辑
          return iCategoryService.addCategory(categoryName,parentId);
       }else{
           return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
       }
    }


    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户没有登录，需要登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            // 更新
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

        @RequestMapping(value = "get_category.do" ,method = RequestMethod.POST)
        @ResponseBody
        public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            if (user == null) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户没有登录，需要登录");
            }
            if (iUserService.checkAdmin(user).isSuccess()) {
                //获取平级category 的子节点的值。
                return iCategoryService.getChildrenParallelCategory(categoryId);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }

       @RequestMapping(value = "get_category_deep.do",method = RequestMethod.POST)
       @ResponseBody
       public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
           User user = (User) session.getAttribute(Const.CURRENT_USER);
           if (user == null) {
               return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户没有登录，需要登录");
           }
           if (iUserService.checkAdmin(user).isSuccess()) {
               //查询当前节点的id和 递归子节点的id
               //0 --->1000---->10000
               return iCategoryService.selectCategoryAndChildrenById(categoryId);
           } else {
               return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
           }
       }
}
