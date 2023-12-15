package com.jh.elastictransservice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.elastictransservice.common.pojo.FieldName;
import com.jh.elastictransservice.mapper.FieldNameMapper;
import com.jh.elastictransservice.service.FieldNameServe;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FieldNameServeImpl implements FieldNameServe {
    @Autowired
    private FieldNameMapper fieldNameMapper;

    @Override
    public PageInfo<FieldName> getFieldByIndex(String indexName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FieldName> byIndex = fieldNameMapper.getByIndex(indexName);
        return new PageInfo<>(byIndex);
    }

    public void setIndexMappingToField (GetIndexResponse indexResponse) {
        Map<String, MappingMetaData> mappings = indexResponse.getMappings();
        mappings.forEach((k,v)->{

        });
    }

}
