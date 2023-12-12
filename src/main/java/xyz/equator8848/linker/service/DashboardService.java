package xyz.equator8848.linker.service;


import xyz.equator8848.linker.model.vo.dashboard.GraphData;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    GraphData countInstanceDelStatus();

    GraphData countProjectDelStatus();

    GraphData countUserStatus();
}
