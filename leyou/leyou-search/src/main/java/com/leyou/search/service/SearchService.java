package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;

import java.io.IOException;

public interface SearchService {

    /**
     * 根据spu转化成Goods
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu) throws IOException;

    /**
     * 根据条件查询
     * @param searchRequest
     * @return
     */
    public SearchResult search(SearchRequest searchRequest);

}
