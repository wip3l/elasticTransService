package com.jh.elastictransservice.service;

import com.jh.elastictransservice.common.dto.IndexCreateDTO;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;

/**
 * @author liqijian
 */
public interface IndexHandleService {
    GetIndexResponse listAllIndex() throws IOException;

    GetIndexResponse getIndexMapping(String indexName) throws IOException;

    CreateIndexResponse createIndex(IndexCreateDTO index) throws IOException;

    AcknowledgedResponse deleteIndex(String indexName) throws IOException;

    void deleteAll(String indexName) throws IOException;
}
