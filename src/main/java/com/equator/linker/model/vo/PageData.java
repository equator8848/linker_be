package com.equator.linker.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;


@Data
public class PageData<T> {
    /**
     * 当前页（前端参数）
     */
    private long pageNum;

    /**
     * 每页的数量（默认30）
     */
    private long size;

    /**
     * 满足条件的总数据量
     */
    private long total;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    /**
     * 数据
     */
    private List<T> data;

    public static <K, T> PageData<T> wrap(Page<K> page, List<T> data) {
        PageData pageData = new PageData();
        pageData.setPageNum(page.getCurrent());
        pageData.setSize(page.getSize());
        pageData.setTotal(page.getTotal());
        if (page.getCurrent() == 1) {
            pageData.setHasNextPage(data.size() < page.getTotal());
        } else {
            pageData.setHasNextPage((page.getCurrent() - 1) * page.getSize() + data.size() < page.getTotal());
        }
        pageData.setData(data);
        return pageData;
    }
}
