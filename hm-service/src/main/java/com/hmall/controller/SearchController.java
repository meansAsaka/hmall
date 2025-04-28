package com.hmall.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.domain.PageDTO;
import com.hmall.domain.dto.ItemDTO;
import com.hmall.domain.po.Item;
import com.hmall.domain.query.ItemPageQuery;
import com.hmall.service.IItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "搜索相关接口")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final IItemService itemService;

    @ApiOperation("搜索商品")
    @GetMapping("/list")
    public PageDTO<ItemDTO> search(@ModelAttribute ItemPageQuery query) {
        // 分页查询
        // System.out.println("query=" + query.toString());
        Long estimatedLastId = (long) ((long) (query.getPageNo() - 1) * query.getPageSize());
        Page<Item> result = itemService.lambdaQuery()
                .like(StrUtil.isNotBlank(query.getKey()), Item::getName, query.getKey())
                .eq(StrUtil.isNotBlank(query.getBrand()), Item::getBrand, query.getBrand())
                .eq(StrUtil.isNotBlank(query.getCategory()), Item::getCategory, query.getCategory())
                .eq(Item::getStatus, 1)
                .between(query.getMaxPrice() != null, Item::getPrice, query.getMinPrice(), query.getMaxPrice())
                .gt(Item::getId, estimatedLastId)
                .page(query.toMpPage("update_time", false));
        // 封装并返回
        //        for (Item item : result.getRecords()) {
        //            System.out.println("item=" + item.toString());
        //        }
        return PageDTO.of(result, ItemDTO.class);
    }
}
