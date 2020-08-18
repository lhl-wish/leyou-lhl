package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.JwtUtils;
import com.leyou.pojo.UserInfo;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public String accredit(String username, String password) {
        // 查询用户
        User user = this.userClient.queryUser(username, password);

        // 判断用户是否存在
        if(user == null){
            return null;
        }

        try {
            // 生成token
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());
            String token = JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
