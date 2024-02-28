package com.jh.dataTransServer.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.dataTransServer.common.pojo.FieldName;
import com.jh.dataTransServer.mapper.FieldNameMapper;
import com.jh.dataTransServer.service.FieldNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldNameServeImpl implements FieldNameService {
    @Autowired
    private FieldNameMapper fieldNameMapper;

    @Override
    public PageInfo<FieldName> getAllFields(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FieldName> fields = fieldNameMapper.getAllFields(pageNum, pageSize);
        PageInfo<FieldName> pageInfo = new PageInfo<>(fields);
        return pageInfo;
    }

    @Override
    public void batchInsert(List<FieldName> userList) {
        fieldNameMapper.batchInsert(userList);
    }

}
