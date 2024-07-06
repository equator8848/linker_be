package xyz.equator8848.linker.service.external;

import xyz.equator8848.linker.model.constant.ScmType;
import xyz.equator8848.linker.service.external.model.BranchInfo;
import xyz.equator8848.linker.service.external.model.CommitInfo;

import java.util.List;

public interface ScmService {
    ScmType getScmType();

    String getApiHostFromRepositoryUrl(String repositoryUrl);

    String getProjectPathFromRepositoryUrl(String repositoryUrl);

    List<BranchInfo> getBranchInfo(String repositoryUrl, String token, String searchKeyword);

    List<CommitInfo> getCommitInfo(String repositoryUrl, String token, String refName);
}
