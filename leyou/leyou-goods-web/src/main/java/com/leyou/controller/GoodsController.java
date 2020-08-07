package com.leyou.controller;

import com.leyou.service.GoodsHtmlService;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 加载数据并跳转至商品详情页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/item/{id}.html")
    public String toItemPage(@PathVariable Long id, Model model){
        // 加载所需数据
        Map<String, Object> map = this.goodsService.loadData(id);
        // 放入模型
        model.addAllAttributes(map);

        goodsHtmlService.createHtml(id);

        return "item";
    }
}
