package com.jh.elastictransservice.service;

import com.jh.elastictransservice.utils.dto.QuerySearchDTO;

import java.util.Map;

/**
 * @author liqijian
 */
public interface ElasticQuerySearchService {
    Map<String, Object> highQuerySearch(QuerySearchDTO dto);

    Map<String, Object> querySearch(QuerySearchDTO dto);
}
