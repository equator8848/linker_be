package xyz.equator8848.linker.service.external.gitlab;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.http.HttpUtil;
import xyz.equator8848.inf.core.model.exception.VerifyException;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.linker.model.constant.ScmType;
import xyz.equator8848.linker.service.external.ScmService;
import xyz.equator8848.linker.service.external.gitlab.model.GitlabBranchInfo;
import xyz.equator8848.linker.service.external.model.BranchInfo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private static final Pattern PROJECT_PATH_PATTERN = Pattern.compile("http[s]?://[^/]+/([^/.]+(?:/[^/.]+)?(?:/[^/.]+)?)(?:\\.git)?");

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
    public List<BranchInfo> getBranchInfo(String repositoryUrl, String token, String searchKeyword) {
        Request request = new Request.Builder()
                .url("%s/api/v4/projects/%s/repository/branches?search=%s"
                        .formatted(getApiHostFromRepositoryUrl(repositoryUrl),
                                getProjectPathFromRepositoryUrl(repositoryUrl),
                                Optional.ofNullable(searchKeyword).orElse("")))
                .addHeader("PRIVATE-TOKEN", token)
                .build();
        try {
            String body = HttpUtil.doRequestGetBody(request);
            List<GitlabBranchInfo> gitlabBranchInfos = JsonUtil.fromJson(body, new TypeReference<>() {
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
