package com.jh.elastictransservice.service;

import com.jh.elastictransservice.utils.dto.IndexCreateDTO;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.io.IOException;

/**
 * @author liqijian
 */
public interface IndexHandleService {
    CreateIndexResponse createIndex(IndexCreateDTO index) throws IOException;

    AcknowledgedResponse deleteIndex(String indexName) throws IOException;

    void deleteAll(String indexName) throws IOException;
}
