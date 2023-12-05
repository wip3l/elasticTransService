package com.jh.elastictransservice.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: Hutao
 * @CreateDate: 2023/11/27 15:38
 */
public interface HdfsService {
    public Map<String, Object> getHdfsCatalog(String path) throws Exception;
    public Map<String ,Object> getHdfsCatalogList(String path,int pageNum,int pageSize) throws Exception;
    public ResponseEntity<InputStreamResource> getHdfsFilePlay(String path) throws Exception;
    public Map<String ,Object> uploadFilesToHdfs(String path, String localFolderPath) throws Exception;
    public Map<String ,Object> createHdfsDirectory(String path) throws Exception;
    public Map<String ,Object> deleteHdfsPath(String path) throws Exception;
}
