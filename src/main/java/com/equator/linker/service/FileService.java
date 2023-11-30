package com.equator.linker.service;

import jakarta.servlet.http.HttpServletResponse;

public interface FileService {
    void fileForward(String url, HttpServletResponse resp);
}
