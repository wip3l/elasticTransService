package com.jh.elastictransservice.common.dto;

import com.jh.elastictransservice.common.vo.IndexCreate;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liqijian
 */
@Data
public class IndexCreateDTO {
    private String indexName;
    private String showName;
    private List<Map<String, String>> mapping;

    public IndexCreateDTO(IndexCreate indexCreate) {
        this.indexName = indexCreate.getIndexName();
        this.showName = indexCreate.getShowName();
        this.mapping = indexCreate.getMapping();
    }
}
