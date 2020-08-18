package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {

    /**
     * 根据条件条件分页查询Spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/spu/page") //key=&saleable=true&page=1&rows=5
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(name = "key",required = false) String key,
            @RequestParam(name = "saleable",required = false) Boolean saleable,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "rows",defaultValue = "5") Integer rows
    );


    /**
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据spuid查询spu
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    @GetMapping("/sku/{skuId}")
    public Sku querySkuById(@PathVariable("skuId") Long skuId);

}
