package com.leyou.service;

public interface GoodsHtmlService {

    /**
     * 生成商品详情的静态页面
     */
    public void createHtml(Long spuId);

    /**
     * 根据id删除商品详情的静态页面
     */
    public void deleteHtml(Long spuId);
}
