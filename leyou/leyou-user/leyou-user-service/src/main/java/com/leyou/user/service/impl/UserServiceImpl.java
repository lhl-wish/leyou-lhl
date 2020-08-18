package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate RedisTemplate;

    private static final String KEY_PREFIX = "user:verify:";

    @Override
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if (type == 1) { // 1:校验用户名
            record.setUsername(data);
        } else if (type == 2) { // 2:校验手机号
            record.setPhone(data);
        }

        return this.userMapper.selectCount(record) == 0;
    }

    @Override
    public Boolean sendVerifyCode(String phone) {
        // 获取 6 位数字验证码
        String code = NumberUtils.generateCode(6);

        try {
            // 发送短信
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("code", code);
//            this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "sms.verify.code", map);
            // 将code放入redis(设置有效时长：5分钟)
            this.RedisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e){
            System.out.println(e.fillInStackTrace());
            return false;
        }

    }

    @Override
    public Boolean register(User user, String code) {
        // 校验验证码
        String redisCode = this.RedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!StringUtils.equals(code,redisCode)){
            return false;
        }

        // 生成盐值
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        // 加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        // 新增用户
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);

        // 删除redis中验证码
        this.RedisTemplate.delete(KEY_PREFIX + user.getPhone());

        return true;
    }

    @Override
    public User queryUser(String username, String password) {
        // 根据username查询用户
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);

        // 判断user是否为空
        if(user == null){
            return null;
        }

        // 获取盐值并给用户输入的密码加盐加密
        password = CodecUtils.md5Hex(password, user.getSalt());

        // 判断是否与查询的user密码一致
        if(StringUtils.equals(password, user.getPassword())){
            return user;
        }
        return null;
    }
}
