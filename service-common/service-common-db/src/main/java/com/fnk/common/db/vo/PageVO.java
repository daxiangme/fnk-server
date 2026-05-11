package com.fnk.common.db.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * <strong>此对象主要是为了解决MybatisPlus的IPage对象在Swagger文档上不显示详情的问题</strong>
 * @author Enigma
 *
 */
@Data
@Schema(name = "PageVO", description = "分页查询结果")
@NoArgsConstructor
public class PageVO<T> {
    @Schema(name = "records", description = "分页查询结果")
    private List<T> records;

    @Schema(name = "total", description = "总记录数")
    private long total;

    @Schema(name = "size", description = "每页数量大小")
    private long size;

    @Schema(name = "current", description = "当前页码")
    private long current;

    @Schema(name = "orders", description = "正序查询")
    private List<OrderItem> orders;

    @Schema(name = "searchCount", description = "是否进行 count 查询")
    private Boolean searchCount;

    @Schema(name = "pages", description = "总页数")
    private long pages;

    public PageVO(IPage<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.orders = page.orders();
        this.searchCount = page.searchCount();
        this.pages = page.getPages();
    }

    public PageVO(IPage<?> page, List<T> records) {
        this.records = records;
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.orders = page.orders();
        this.searchCount = page.searchCount();
        this.pages = page.getPages();
    }
}
