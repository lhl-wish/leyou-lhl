package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return
     */
    List<Category> queryCategoriesByPid(Long pid);

    /**
     * 根据ID集合查询分类名集合
     * @param ids
     * @return
     */
    List<String> queryNamesByIds(List<Long> ids);
}
