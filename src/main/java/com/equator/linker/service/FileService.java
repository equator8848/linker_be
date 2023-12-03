package com.equator.linker.service;

import jakarta.servlet.http.HttpServletResponse;
import okhttp3.Headers;

import javax.annotation.Nullable;

public interface FileService {
    void fileForward(String url,String fileName, Headers headers, HttpServletResponse resp);

    void downloadInstanceArtifact(Long instanceId, @Nullable String fileName, HttpServletResponse resp);
}
