package com.jh.elastictransservice.service.Impl;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.jh.elastictransservice.service.DataTransService;
import com.jh.elastictransservice.utils.ElasticClientUtils;
import com.jh.elastictransservice.utils.dto.CsvToEsDTO;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liqijian
 */
@Service
public class DataTransServiceImpl implements DataTransService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ElasticClientUtils elasticClientUtils;

    @Override
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
            if (csvToEsDTO.getIsCustomTitle()) {
                headers = csvToEsDTO.getTitle();
                csvReader.setHeaders(headers);
            } else {
                csvReader.readHeaders();
                headers = csvReader.getHeaders();
            }
            if (headers.length < 1) {
                throw new RuntimeException("请检查csv文件Title配置");
            }
            //遍历csv文件行
            while (csvReader.readRecord()) {
                HashFunction hf = Hashing.murmur3_128();
                String s1 = hf.newHasher().putString(csvReader.get(0)+csvReader.get(1)+csvReader.get(2), Charsets.UTF_8).hash().toString();
                UpdateRequest updateRequest = new UpdateRequest(csvToEsDTO.getIndexName(), s1
                        );
                Map<String, Object> jsonMap = new HashMap<>();
                for (String header : headers) {
                    jsonMap.put(header, csvReader.get(header));
                }
                updateRequest.doc(jsonMap);
                updateRequest.docAsUpsert(true);
                elasticClient.update(updateRequest, RequestOptions.DEFAULT);
                //System.out.println(csvReader.get(0));
            }

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
