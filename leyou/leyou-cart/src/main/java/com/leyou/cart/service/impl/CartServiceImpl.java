package com.leyou.cart.service.impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import com.leyou.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String KEY_PREFIX = "user:cart:";

    @Autowired
    private GoodsClient goodsClient;

    @Override
    public void addCart(Cart cart) {
        // 获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // redis的key
        String key = KEY_PREFIX + userInfo.getId();
        // 查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);
        
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();
        // 判断当前商品是否在购物车中
        if(hashOperations.hasKey(skuId)){
            // 在，更新数量
            String cartJson = hashOperations.get(skuId).toString();
            // 反序列化
            cart = JsonUtils.parse(cartJson, Cart.class);
            // 新增数量
            cart.setNum(cart.getNum() + num);
        } else {
            // 不在新增购物车
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setImage(StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(),",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
        }
        // 将cart序列化字符串放入redis
        hashOperations.put(skuId, JsonUtils.serialize(cart));
    }

    @Override
    public List<Cart> queryCarts() {
        // 获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 查询用户购物车
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        // 判断是否有购物车记录
        List<Object> cartsJson = hashOperations.values();

        // 如果购物车集合为空，直接返回null
        if(CollectionUtils.isEmpty(cartsJson)){
            return null;
        }

        // 将List<Object>转化为List<Cart>
        return cartsJson.stream().map(cart -> JsonUtils.parse(cart.toString(), Cart.class)).collect(Collectors.toList());
    }

    @Override
    public void updateCart(Cart cart) {
        // 获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 查询用户购物车
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        // 判断当前商品是否在购物车中
        if(!hashOperations.hasKey(cart.getSkuId().toString())){
            return ;
        }

        // 获取购物车Json字符串
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        // 反序列化
        Cart cart1 = JsonUtils.parse(cartJson, Cart.class);
        // 更新数量
        cart1.setNum(cart.getNum());
        // 写入购物车
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart1));
    }

    @Override
    public void deleteCart(String skuId) {
        // 获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 获取购物车信息
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        // 删除商品
        hashOperations.delete(skuId);
    }
}
