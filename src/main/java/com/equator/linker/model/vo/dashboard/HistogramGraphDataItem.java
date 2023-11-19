package com.equator.linker.model.vo.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class HistogramGraphDataItem {
    private String name;

    private List<Float> data;
}
