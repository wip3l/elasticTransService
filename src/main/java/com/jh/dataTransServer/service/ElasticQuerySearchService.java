package com.jh.dataTransServer.service;

import com.jh.dataTransServer.common.dto.QuerySearchDTO;

import java.util.Map;

/**
 * @author liqijian
 */
public interface ElasticQuerySearchService {
    Map<String, Object> highQuerySearch(QuerySearchDTO dto);

    Map<String, Object> querySearch(QuerySearchDTO dto);
}
