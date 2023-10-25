package com.equator.linker.model.dto;


import com.equator.core.dynamic.config.CommaSeparatedItem;
import com.equator.core.dynamic.config.ModelTransformerField;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Author: Equator
 * @Date: 2022/8/14 22:49
 **/
@Data
public class DynamicAppConfiguration {
    @ModelTransformerField(nullable = true)
    private long version;

    @Deprecated
    @CommaSeparatedItem(collectionType = Set.class, itemType = String.class)
    @ModelTransformerField(defaultValue = "equator")
    private Set<String> admins;

    /**
     * 是否限制实例网速
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean networkTrafficLimit;

    /**
     * 是否网络隔离
     */
    @ModelTransformerField(defaultValue = "true")
    private Boolean networkIsolation;

    /**
     * LXD实例监控面板
     */
    @ModelTransformerField(defaultValue = "#")
    private String lxdClusterGrafanaUrl;

    /**
     * 主机监控面板
     */
    @ModelTransformerField(defaultValue = "#")
    private String clusterHostGrafanaUrl;

    /**
     * CEPH监控面板
     */
    @ModelTransformerField(defaultValue = "#")
    private String cephMgrGrafanaUrl;

    /**
     * 自定义硬盘监控
     */
    @ModelTransformerField(defaultValue = "#")
    private String diskGrafanaUrl;

    /**
     * LXD自定义监控面板
     */
    @ModelTransformerField(defaultValue = "#")
    private String lxdGrafanaUrl;

    /**
     * 普罗米修斯面板
     */
    @ModelTransformerField(defaultValue = "#")
    private String prometheusUrl;

    /**
     * 报警管理面板
     */
    @ModelTransformerField(defaultValue = "#")
    private String alertManagerUrl;

    /**
     * 限制用户每天可以软删除次数
     */
    @ModelTransformerField(defaultValue = "8")
    private Integer softDeleteLimitThreeDay;

    /**
     * 用于免密登录
     */
    @ModelTransformerField(defaultValue = "")
    private String lxdClusterHostGrafanaApiKey;

    /**
     * 是否自动关闭过期实例
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean autoStopExpiredInstances;

    /**
     * 是否自动删除待删除的存储卷
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean autoDeleteVolumeSwitch;

    /**
     * 自动删除待删除的存储卷 延时秒数
     */
    @ModelTransformerField(defaultValue = "3600")
    private Integer autoDeleteVolumeDelaySecond;

    /**
     * 是否自动删除待删除的实例
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean autoDeleteInstanceSwitch;

    /**
     * 是否自动删除不存在集群的实例
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean autoDeleteInstanceNotExistedSwitch;

    /**
     * 是否自动删除不存在集群的存储卷
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean autoDeleteVolumeNotExistedSwitch;

    /**
     * 自动删除待删除的实例 延时秒数
     */
    @ModelTransformerField(defaultValue = "3600")
    private Integer autoDeleteInstanceDelaySecond;

    /**
     * 是否自动删除过期的实例
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean autoSoftDeleteInstanceSwitch;

    /**
     * 自动删除过期的实例 延时秒数
     */
    @ModelTransformerField(defaultValue = "3600")
    private Integer autoSoftDeleteInstanceDelaySecond;

    /**
     * 计算节点接收到警报时，交换空间使用率是多少会触发WX公众号推送报警
     */
    @ModelTransformerField(defaultValue = "70")
    private Integer wxNoticeSwapUsageThreshold;

    /**
     *
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean wxNoticeSwitch;

    /**
     * 数据库备份时，忽略哪些表
     */
    @CommaSeparatedItem(collectionType = List.class, itemType = String.class)
    @ModelTransformerField(defaultValue = "tb_operation_history")
    private List<String> dataBackupIgnoreTables;

    /**
     * 用户表中的管理员ID
     */
    @CommaSeparatedItem(collectionType = List.class, itemType = Integer.class)
    @ModelTransformerField(defaultValue = "153")
    private List<Integer> adminUserIds;

    /**
     * 紧急通知
     */
    @ModelTransformerField(defaultValue = "", nullable = true)
    private String urgencyNotice;

    /**
     * 常驻通知
     */
    @ModelTransformerField(defaultValue = "", nullable = true)
    private String residentNotice;

    /**
     * 运行中的实例超过这个阈值，节点判断为拥挤
     */
    @ModelTransformerField(defaultValue = "100")
    private Integer nodeCrowdedThreshold;

    /**
     * 是否需要提示登录次数过多阈值
     */
    @ModelTransformerField(defaultValue = "3")
    private Integer needTipLoginFailNumThreshold;
}
