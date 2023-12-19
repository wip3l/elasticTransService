package com.jh.elastictransservice.service;

import com.github.pagehelper.PageInfo;
import com.jh.elastictransservice.common.pojo.FieldName;

import java.util.List;

public interface FieldNameService {
    PageInfo<FieldName> getAllFields(int pageNum, int pageSize);

    void batchInsert(List<FieldName> userList);
}
