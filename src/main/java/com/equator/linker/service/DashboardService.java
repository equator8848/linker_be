package com.equator.linker.service;


import com.equator.linker.model.vo.dashboard.GraphData;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    GraphData countInstanceDelStatus();

    GraphData countProjectDelStatus();

    GraphData countUserStatus();
}
