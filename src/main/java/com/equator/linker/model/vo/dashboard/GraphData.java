package com.equator.linker.model.vo.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class GraphData {
    private String title;

    private List<GraphDataItem> dataItems;
}
