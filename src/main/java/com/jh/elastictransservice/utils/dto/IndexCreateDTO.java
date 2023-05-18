package com.jh.elastictransservice.utils.dto;

import com.jh.elastictransservice.utils.vo.IndexCreate;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liqijian
 */
@Data
public class IndexCreateDTO {
    private String indexName;
    private List<Map<String, String>> mapping;

    public IndexCreateDTO(IndexCreate indexCreate) {
        this.indexName = indexCreate.getIndexName();
        this.mapping = indexCreate.getMapping();
    }
}
