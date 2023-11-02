package com.jh.elastictransservice.service.Impl;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jh.elastictransservice.service.DataTransService;
import com.jh.elastictransservice.utils.DataTransUtils;
import com.jh.elastictransservice.utils.ElasticClientUtils;
import com.jh.elastictransservice.utils.dto.CsvToEsDTO;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author liqijian
 */
@Service
public class DataTransServiceImpl implements DataTransService {

    @Value("${elasticsearch.bulkSize}")
    private int bulkSize;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ElasticClientUtils elasticClientUtils;
    @Autowired
    private DataTransUtils dataTransUtils;

    @Override
    @Async
    public void csvToEs(CsvToEsDTO csvToEsDTO) {
        //初始化client
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        //判断index是否存在
        GetIndexRequest request = new GetIndexRequest(csvToEsDTO.getIndexName());
        try {
            if (!elasticClient.indices().exists(request, RequestOptions.DEFAULT)) {
                throw new RuntimeException("index不存在，请先创建index");
            }
        } catch (IOException e) {
            throw new RuntimeException("无法确认index是否存在",e);
        }

        //读取csv文件
        try {
            CsvReader csvReader = new CsvReader(csvToEsDTO.getCsvPath(), csvToEsDTO.getSplitWord().charAt(0),
                    StandardCharsets.UTF_8);
            //设置header
            String[] headers;
            String[] oriHeader;
            if (csvToEsDTO.getIsCustomTitle()) {
                headers = csvToEsDTO.getTitle();
                csvReader.setHeaders(headers);
                oriHeader = new String[]{"自定义表头时无原始表头"};
            } else {
                csvReader.readHeaders();
                oriHeader = csvReader.getHeaders();
                if (csvToEsDTO.getIsTitleHasCh()) {
                    headers = dataTransUtils.ch2py(oriHeader);
                } else {
                    headers = oriHeader;
                }
                csvReader.setHeaders(headers);
            }
            if (headers.length < 1) {
                throw new RuntimeException("请检查csv文件Title配置");
            }
            //遍历csv文件行
            while (csvReader.readRecord()) {
//                HashFunction hf = Hashing.murmur3_128();
//                String s1 = hf.newHasher().putString(csvReader.get(0)+csvReader.get(1)+csvReader.get(2), Charsets.UTF_8).hash().toString();
                String s1 = UUID.randomUUID().toString();
                UpdateRequest updateRequest = new UpdateRequest(csvToEsDTO.getIndexName(), s1
                        );
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("oriHeader", Arrays.toString(oriHeader));
                for (String header : headers) {
                    jsonMap.put(header, csvReader.get(header));
                }
                updateRequest.doc(jsonMap);
                updateRequest.docAsUpsert(true);
                elasticClient.update(updateRequest, RequestOptions.DEFAULT);
            }

        } catch (IOException e) {
            log.error("解析csv文件失败", e);
            throw new RuntimeException("解析csv文件失败",e);
        }
    }


    @Override
    @Async
    public void csvFoldToEs(CsvToEsDTO csvToEsDTO) {
        String path = csvToEsDTO.getCsvPath();
        Set<File> files = dataTransUtils.listFiles(path);
        for (File f : files) {
            csvToEsDTO.setCsvPath(f.getPath());
            csvToEsBulk(csvToEsDTO);
        }
    }

    @Override
    @Async
    public void csvDeepFoldToEs(CsvToEsDTO csvToEsDTO) {
        String path = csvToEsDTO.getCsvPath();
        Set<File> files = dataTransUtils.listFiles(path);
        for (File f : files) {
            csvToEsDTO.setCsvPath(f.getPath());
            csvToEsBulk(csvToEsDTO);
        }
    }

    @Override
    @Async
    public void csvToEsBulk(CsvToEsDTO csvToEsDTO) {
        //初始化client
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        //判断index是否存在
        GetIndexRequest request = new GetIndexRequest(csvToEsDTO.getIndexName());
        try {
            if (!elasticClient.indices().exists(request, RequestOptions.DEFAULT)) {
                throw new RuntimeException("index不存在，请先创建index");
            }
        } catch (IOException e) {
            throw new RuntimeException("无法确认index是否存在",e);
        }

        //读取csv文件
        try {
            CsvReader csvReader = new CsvReader(csvToEsDTO.getCsvPath(), csvToEsDTO.getSplitWord().charAt(0),
                    StandardCharsets.UTF_8);
            //设置header
            String[] headers;
            String[] oriHeader;
            if (csvToEsDTO.getIsCustomTitle()) {
                headers = csvToEsDTO.getTitle();
                csvReader.setHeaders(headers);
                oriHeader = new String[]{"自定义表头时无原始表头"};
            } else {
                csvReader.readHeaders();
                oriHeader = csvReader.getHeaders();
                if (csvToEsDTO.getIsTitleHasCh()) {
                    headers = dataTransUtils.ch2py(oriHeader);
                } else {
                    headers = oriHeader;
                }
                csvReader.setHeaders(headers);
            }
            if (headers.length < 1) {
                throw new RuntimeException("请检查csv文件Title配置");
            }

            //创建请求
            BulkRequest bulkRequest = new BulkRequest();
            //创建index请求
            IndexRequest requestData = null;

            long startTimeMillis = System.currentTimeMillis();
            int total = 0;
            //遍历csv文件行
            while (csvReader.readRecord()) {
//                HashFunction hf = Hashing.murmur3_128();
//                String s1 = hf.newHasher().putString(csvReader.get(0)+csvReader.get(1)+csvReader.get(2), Charsets.UTF_8).hash().toString();
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("oriHeader", Arrays.toString(oriHeader));
                for (String header : headers) {
                    jsonMap.put(header, csvReader.get(header));
                }
                requestData = new IndexRequest(csvToEsDTO.getIndexName(), "_doc", UUID.randomUUID().toString()).source(jsonMap, XContentType.JSON);
                bulkRequest.add(requestData);
                //设置索引刷新规则
                bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
                //分批次提交，数量控制
                if (bulkRequest.numberOfActions() % bulkSize == 0) {
                    total = total + bulkRequest.numberOfActions();
                    log.info("缓存数据达到bulkSize阈值:{} 开始本次提交", bulkRequest.numberOfActions());
                    BulkResponse bulkResponse = elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                    log.info("es同步数据结果是否出错:{}", bulkResponse.hasFailures());
                    log.info("es一共同步数据数量:{}", total);
                    bulkRequest = new BulkRequest();
                }
            }
            //最后提交
            log.info("提交最后一批数据:{} ", bulkRequest.numberOfActions());
            total = total + bulkRequest.numberOfActions();
            BulkResponse bulkResponse = elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("es同步数据结果是否出错:{}", bulkResponse.hasFailures());
            long endTimeMillis = System.currentTimeMillis();
            log.info("es本次同步数据数量:{}", total);
            log.info("es同步数据耗时:{} ms", endTimeMillis - startTimeMillis);

        } catch (IOException e) {
            log.error("解析csv文件失败", e);
            throw new RuntimeException("解析csv文件失败",e);
        }
    }
    @Override
    public void deleteRow(String indexName, String id) throws IOException {
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        elasticClient.delete(new DeleteRequest(indexName, id), RequestOptions.DEFAULT);
    }

    @Override
    public void newCsv(String fileName) throws IOException {
        CsvWriter csvWriter = new CsvWriter(fileName, ',',StandardCharsets.UTF_8);
        String[] header = {"name","gender","age"};
        String[] content = {"lihua","female","24"};
        csvWriter.writeRecord(header);
        csvWriter.writeRecord(content);
        csvWriter.close();
    }

}
