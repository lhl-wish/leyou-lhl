package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/brand")
public interface BrandApi {


    /**
     * 根据查询分页并排序查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/page") //page?key=&page=1&rows=5&sortBy=id&desc=false
    public PageResult<Brand> queryBrandsByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
    );

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);
}
