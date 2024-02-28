package com.jh.dataTransServer.service.Impl;

import com.jh.dataTransServer.service.ElasticQuerySearchService;
import com.jh.dataTransServer.utils.ElasticSearchUtils;
import com.jh.dataTransServer.common.dto.QuerySearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liqijian
 */
@Service
public class ElasticQuerySearchServiceImpl implements ElasticQuerySearchService {
    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Override
    public Map<String, Object> highQuerySearch(QuerySearchDTO dto) {
        return elasticSearchUtils.queryForEs(dto.getIndexName(), dto.getEqualsCondition(),
                dto.getRangeCondition(), dto.getOrderBy(), dto.getPageNum(), dto.getPageSize());
    }

    @Override
    public Map<String, Object> querySearch(QuerySearchDTO dto) {
        return elasticSearchUtils.queryKeyword(dto.getIndexName(), dto.getKeyWord(),
                dto.getPageNum(), dto.getPageSize());
    }


}
