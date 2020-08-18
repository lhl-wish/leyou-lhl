package com.leyou.user.service;

import com.leyou.user.pojo.User;

public interface UserService {

    /**
     * 校验用户：手机号、用户名
     * @param data
     * @param type
     * @return
     */
    Boolean checkUser(String data, Integer type);

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    Boolean register(User user, String code);

    /**
     * 查询用户（登录）
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);
}
