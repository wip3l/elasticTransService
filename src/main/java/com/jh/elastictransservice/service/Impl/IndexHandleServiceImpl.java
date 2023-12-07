package com.jh.elastictransservice.service.Impl;

import com.jh.elastictransservice.service.IndexHandleService;
import com.jh.elastictransservice.utils.ElasticClientUtils;
import com.jh.elastictransservice.common.dto.IndexCreateDTO;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liqijian
 */
@Service
public class IndexHandleServiceImpl implements IndexHandleService {
    @Autowired
    private ElasticClientUtils elasticClientUtils;


    @Override
    public GetIndexResponse listAllIndex () throws IOException {
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        GetIndexRequest request = new GetIndexRequest("*");
        GetIndexResponse getIndexResponse = elasticClient.indices().get(request, RequestOptions.DEFAULT);
        elasticClient.close();
        return getIndexResponse;
    }

    @Override
    public GetIndexResponse getIndexMapping(String indexName) throws IOException {
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        GetIndexResponse getIndexResponse = elasticClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        elasticClient.close();
        return getIndexResponse;
    }

    @Override
    public CreateIndexResponse createIndex(IndexCreateDTO index) throws IOException {
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        CreateIndexRequest request = new CreateIndexRequest(index.getIndexName());
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );

        //mapping
        Map<String, Object> mapping = new HashMap<>();
        //properties
        Map<String, Object> properties = new HashMap<>();

        //遍历mapping
        if (Objects.nonNull(index.getMapping())) {
            index.getMapping().forEach(map -> {
                map.forEach((k, v) -> {
                    Map<String, String> key = new HashMap<>();
                    key.put("type", v);
                    properties.put(k, key);
                });
            });
        }

        mapping.put("properties", properties);
        request.mapping(mapping);
        CreateIndexResponse createIndexResponse = elasticClient.indices().create(request, RequestOptions.DEFAULT);
        elasticClient.close();
        return createIndexResponse;
    }

    @Override
    public AcknowledgedResponse deleteIndex(String indexName) throws IOException {
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        AcknowledgedResponse delete = elasticClient.indices().
                delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
        elasticClient.close();
        return delete;
    }

    @Override
    public HashMap<Object, Object> docStatic() throws IOException {
        HashMap<Object, Object> docStaticMap = new HashMap<>();
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        GetIndexRequest request = new GetIndexRequest("*");
        GetIndexResponse getIndexResponse = elasticClient.indices().get(request, RequestOptions.DEFAULT);
        String[] indexNames = getIndexResponse.getIndices();
        for (String indexName : indexNames) {
            CountRequest countRequest = new CountRequest(indexName);
            CountResponse countResponse = elasticClient.count(countRequest, RequestOptions.DEFAULT);
            long count = countResponse.getCount();
            docStaticMap.put(indexName,count);
        }
        elasticClient.close();
        return docStaticMap;
    }

    @Override
    @Async
    public void deleteAll (String indexName) throws IOException {
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest (indexName);
        deleteRequest.setQuery (QueryBuilders.matchAllQuery());
        deleteRequest.setRefresh(true);
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        elasticClient.deleteByQuery(deleteRequest,RequestOptions.DEFAULT);
        elasticClient.close();
    }

}
