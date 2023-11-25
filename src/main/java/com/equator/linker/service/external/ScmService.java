package com.equator.linker.service.external;

import com.equator.linker.model.constant.ScmType;
import com.equator.linker.service.external.model.BranchInfo;

import java.util.List;

public interface ScmService {
    ScmType getScmType();

    String getApiHostFromRepositoryUrl(String repositoryUrl);

    String getProjectPathFromRepositoryUrl(String repositoryUrl);

    List<BranchInfo> getBranchInfo(String repositoryUrl, String token);
}
