package xyz.equator8848.linker.model.vo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBranchResult {
    private List<ProjectBranchInfo> projectBranchInfos;

    /**
     * 是否是假数据
     */
    private Boolean isDefaultData;
}
