package com.lhl.search.test;

import com.leyou.LeyouSearchApplication;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = LeyouSearchApplication.class)
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void test(){
        // 创建索引库
        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);

        Integer page = 1;
        Integer rows = 100;

        do {
            // 分页查询spu,获取分页结果集
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, null, page, rows);
            // 获取当前页数据
            List<SpuBo> spuBos = result.getItems();
            // 处理List<SouBo>  =>  List<Goods>
            List<Goods> goodsList = spuBos.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            // 新增数据
            this.goodsRepository.saveAll(goodsList);

            rows = spuBos.size();
            page++;

        }while (rows == 100);
    }
}
