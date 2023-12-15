package com.jh.elastictransservice.service;

import com.github.pagehelper.PageInfo;
import com.jh.elastictransservice.common.pojo.FieldName;

public interface FieldNameServe {
    PageInfo<FieldName> getFieldByIndex(String indexName, int pageNum, int pageSize);
}
