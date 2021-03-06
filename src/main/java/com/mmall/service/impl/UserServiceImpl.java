package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
       int resultCount = userMapper.checkUsername(username);
       if(resultCount == 0){
           return ServerResponse.createByErrorMessage("用户名不存在");
       }
       //todo 密码MD5加密  TODO这是个好习惯
        String  md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
       if(user ==null){
           return  ServerResponse.createByErrorMessage("密码错误");
       }
       // org.apache.commons.lang3.StringUtils
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    public ServerResponse<String> register(User user){
        //代码复用
        ServerResponse<String> checkValid = checkValid(user.getUsername(), Const.USERNAME);
        //如果校验失败 ，就返回信息
        if(!checkValid.isSuccess()){
            return checkValid;
        }
        checkValid = checkValid(user.getEmail(),Const.EMAIL);
        //如果校验失败 ，就返回信息
        if(!checkValid.isSuccess()){
            return checkValid;
        }
        //md5 密码加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        int insert = userMapper.insert(user);
        if(insert == 0){
            return ServerResponse.createByErrorMessage("注册失败!");
        }
        return  ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
       return ServerResponse.createBySuccessMessage("校验成功");
    }


    public ServerResponse<String> selectQuestion(String username){
        ServerResponse responseValid = this.checkValid(username,Const.USERNAME);
        //这里的逻辑判断用户存不存在。
        if(responseValid.isSuccess()){
            //用户不存在  这里的逻辑有点转
            //不是很明白----解释：如果是成功的话这样的 ServerResponse.createBySuccessMessage("校验成功");失败是用户存在。
            //但是这是不符合要求的所以，重新定义返回值。
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestion(username);
       if(StringUtils.isNotBlank(question)){
           return ServerResponse.createBySuccess(question);
       }
       return ServerResponse.createByErrorMessage("找回密码的问题的是空的");
    }


    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount >0){
            //说明问题答案都是正确的 ，需要放到缓存中。
            String forgerToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgerToken);
            return ServerResponse.createBySuccess(forgerToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误！");
    }



    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
       if(StringUtils.isBlank(forgetToken)){
           return ServerResponse.createByErrorMessage("参数错误,token 需要传递");
       }
        ServerResponse responseValid = this.checkValid(username,Const.USERNAME);
        if(responseValid.isSuccess()){
            //成功代表用户不存在。
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token 无效或过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(resultCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误，请重新获取密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){

        //防止横向越权，一定要验证用户的旧密码，一定要指定是当前这个用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int  updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    public ServerResponse<User> updateInformation(User user){
        //username 不能进行一个更新
        //email 要进行一个校验，校验新的email的是不是存在，并且email不是当前用户的email的。
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resultCount > 0){
             return ServerResponse.createByErrorMessage("邮箱已经存在，请更换email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setPhone(user.getPhone());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount >0){
            return ServerResponse.createBySuccess("更新用户信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户没有找到");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    //backend

    /**
     * 校验是否管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdmin(User user){
        if(user != null && user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
       return ServerResponse.createByError();
    }
 }
