package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    /**
     *
     * @param username
     * @param password
     * @return ServerResponse<User>
     */
    ServerResponse<User> login(String username, String password);

    /**
     *
     * @param user
     * @return ServerResponse<String>
     */
    ServerResponse<String> register(User user);

    /**
     *
     * @param str
     * @param type
     * @return  ServerResponse<String>
     */
    ServerResponse<String> checkValid(String str,String type);

    /**
     *
     * @param username
     * @return ServerResponse<String>
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     *
     * @param username
     * @param question
     * @param answer
     * @return  ServerResponse<String>
     */
    ServerResponse<String> checkAnswer(String username,String question,String answer);

    /**
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return  ServerResponse<String>
     */
    ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken);

    /**
     *
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return ServerResponse<String>
     */
    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    /**
     *
     * @param user
     * @return ServerResponse<User>
     */
    ServerResponse<User> updateInformation(User user);

    /**
     *
     * @param userId
     * @return ServerResponse<User>
     */
    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdmin(User user);
}
