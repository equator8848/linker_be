package com.equator.linker.service.external.gitlab;

import com.equator.core.http.HttpUtil;
import com.equator.core.model.exception.VerifyException;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.model.constant.ScmType;
import com.equator.linker.service.external.ScmService;
import com.equator.linker.service.external.gitlab.model.GitlabBranchInfo;
import com.equator.linker.service.external.model.BranchInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GitlabScmService implements ScmService {
    @Override
    public ScmType getScmType() {
        return ScmType.GITLAB;
    }

    private static final Pattern API_HOST_PATTERN = Pattern.compile("(http[s]?://[^/]+)");

    @Override
    public String getApiHostFromRepositoryUrl(String repositoryUrl) {
        Matcher matcher = API_HOST_PATTERN.matcher(repositoryUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new VerifyException("无法从仓库地址获取API地址");
        }
    }

    private static final Pattern PROJECT_PATH_PATTERN = Pattern.compile("http[s]?://[^/]+/([^/.]+/[^/.]+)(?:\\.git)?");

    @Override
    public String getProjectPathFromRepositoryUrl(String repositoryUrl) {
        Matcher matcher = PROJECT_PATH_PATTERN.matcher(repositoryUrl);
        if (matcher.find()) {
            return URLEncoder.encode(matcher.group(1), StandardCharsets.UTF_8);
        } else {
            throw new VerifyException("无法从仓库地址获取API地址");
        }
    }

    @Override
    public List<BranchInfo> getBranchInfo(String repositoryUrl, String token) {
        Request request = new Request.Builder()
                .url("%s/api/v4/projects/%s/repository/branches".formatted(getApiHostFromRepositoryUrl(repositoryUrl),
                        getProjectPathFromRepositoryUrl(repositoryUrl)))
                .addHeader("PRIVATE-TOKEN", token)
                .build();
        try {
            List<GitlabBranchInfo> gitlabBranchInfos = JsonUtil.fromJson(HttpUtil.doRequestGetBody(request), new TypeReference<>() {
            });
            return gitlabBranchInfos.stream().map(gitlabBranchInfo -> {
                BranchInfo branchInfo = new BranchInfo();
                branchInfo.setName(gitlabBranchInfo.getName());
                GitlabBranchInfo.Commit commit = gitlabBranchInfo.getCommit();
                if (commit == null) {
                    return branchInfo;
                }
                branchInfo.setLatestCommitId(commit.getShortId());
                branchInfo.setLatestCommitTitle(commit.getTitle());
                branchInfo.setLatestCommitUser(commit.getAuthorName());
                branchInfo.setLatestCommitDate(commit.getCommittedDate());
                return branchInfo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("getBranchInfo failed {} ", repositoryUrl, e);
            return Collections.emptyList();
        }
    }
}
