package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    /**
     * 文件上传
     * @param file
     * @param path
     * @return String 为文件名
     */
    String upload(MultipartFile file , String path);
}
