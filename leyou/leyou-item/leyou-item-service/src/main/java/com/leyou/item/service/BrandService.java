package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

public interface BrandService {

    /**
     * 根据查询分页并排序查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);
}
