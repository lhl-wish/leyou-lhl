package com.leyou.service;

import java.util.Map;

public interface GoodsService {

    /**
     * 根据spuId加载数据模型
     * @param spuId
     * @return
     */
    public Map<String, Object> loadData(Long spuId);
}
