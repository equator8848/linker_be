package xyz.equator8848.linker.service.impl;


import xyz.equator8848.linker.dao.service.ProjectDaoService;
import xyz.equator8848.linker.dao.service.UserDaoService;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.vo.dashboard.CountGroupResult;
import xyz.equator8848.linker.model.vo.dashboard.GraphData;
import xyz.equator8848.linker.model.vo.dashboard.GraphDataItem;
import xyz.equator8848.linker.service.DashboardService;
import xyz.equator8848.linker.dao.service.InstanceDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private ProjectDaoService projectDaoService;


    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private UserDaoService userDaoService;

    @Override
    public GraphData countInstanceDelStatus() {
        List<CountGroupResult> countGroupResults = instanceDaoService.getBaseMapper().countInstanceDelStatus();
        GraphData graphData = new GraphData();
        graphData.setTitle("实例状态饼图");
        graphData.setDataItems(countGroupResults.stream().map(e -> {
            GraphDataItem graphDataItem = new GraphDataItem();
            graphDataItem.setName(ModelStatus.DelFlag.getStatusStr(e.getStatus()));
            graphDataItem.setValue(Float.valueOf(e.getCountNum()));
            return graphDataItem;
        }).collect(Collectors.toList()));
        return graphData;
    }

    @Override
    public GraphData countProjectDelStatus() {
        List<CountGroupResult> countGroupResults = projectDaoService.getBaseMapper().countProjectDelStatus();
        GraphData graphData = new GraphData();
        graphData.setTitle("项目状态饼图");
        graphData.setDataItems(countGroupResults.stream().map(e -> {
            GraphDataItem graphDataItem = new GraphDataItem();
            graphDataItem.setName(ModelStatus.DelFlag.getStatusStr(e.getStatus()));
            graphDataItem.setValue(Float.valueOf(e.getCountNum()));
            return graphDataItem;
        }).collect(Collectors.toList()));
        return graphData;
    }

    @Override
    public GraphData countUserStatus() {
        List<CountGroupResult> countGroupResults = userDaoService.getBaseMapper().countUserStatus();
        GraphData graphData = new GraphData();
        graphData.setTitle("用户状态饼图");
        graphData.setDataItems(countGroupResults.stream().map(e -> {
            GraphDataItem graphDataItem = new GraphDataItem();
            graphDataItem.setName(ModelStatus.UserStatus.getUserStatusStr(e.getStatus()));
            graphDataItem.setValue(Float.valueOf(e.getCountNum()));
            return graphDataItem;
        }).collect(Collectors.toList()));
        return graphData;
    }
}
