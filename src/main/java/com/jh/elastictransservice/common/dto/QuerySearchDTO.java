package com.jh.elastictransservice.common.dto;

import com.jh.elastictransservice.common.vo.QuerySearch;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liqijian
 */
@Data
public class QuerySearchDTO {
    String indexName;
    String keyWord;
    Map<String, Object> equalsCondition;
    Map<String, Object> rangeCondition;
    List<String> orderBy;
    Integer pageNum;
    Integer pageSize;
    public QuerySearchDTO (QuerySearch querySearch) {
        this.indexName = querySearch.getIndexName();
        this.keyWord = querySearch.getKeyWord();
        this.equalsCondition = querySearch.getEqualsCondition();
        this.rangeCondition = querySearch.getRangeCondition();
        this.orderBy = querySearch.getOrderBy();
        this.pageNum = querySearch.getPageNum();
        this.pageSize = querySearch.getPageSize();
    }
}
