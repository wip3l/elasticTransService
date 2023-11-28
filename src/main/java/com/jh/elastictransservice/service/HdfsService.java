package com.jh.elastictransservice.service;

import java.util.Map;

/**
 * @Author: Hutao
 * @CreateDate: 2023/11/27 15:38
 */
public interface HdfsService {
    public Map<String, Object> getHdfsCatalog(String path) throws Exception;
}
