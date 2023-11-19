package com.equator.linker.model.vo.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class HistogramGraphData {
    private String title;

    private List<String> mainDimList;

    private List<HistogramGraphDataItem> seriesDataList;
}
