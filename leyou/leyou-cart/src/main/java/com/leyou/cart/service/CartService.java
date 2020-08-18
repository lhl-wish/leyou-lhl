package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

public interface CartService {


    void addCart(Cart cart);

    List<Cart> queryCarts();

    void updateCart(Cart cart);

    void deleteCart(String skuId);
}
