package com.jh.elastictransservice.service.Impl;

import cn.hutool.core.io.IoUtil;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jh.elastictransservice.common.dto.CsvToEsDTO;
import com.jh.elastictransservice.common.pojo.FieldName;
import com.jh.elastictransservice.common.pojo.TaskInfo;
import com.jh.elastictransservice.mapper.FieldNameMapper;
import com.jh.elastictransservice.mapper.TaskInfoMapper;
import com.jh.elastictransservice.service.DataTransService;
import com.jh.elastictransservice.utils.DataTransUtils;
import com.jh.elastictransservice.utils.ElasticClientUtils;
import com.jh.elastictransservice.utils.FileEncodeUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
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

import java.io.*;
import java.nio.charset.Charset;
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
    @Autowired
    private TaskInfoMapper taskInfoMapper;
    @Autowired
    private FieldNameMapper fieldNameMapper;


    @Override
    @Async
    public void csvFoldToEs(CsvToEsDTO csvToEsDTO) throws IOException {
        String path = csvToEsDTO.getCsvPath();
        Set<File> files = dataTransUtils.listFiles(path);
        for (File f : files) {
            csvToEsDTO.setCsvPath(f.getPath());
            log.info("开始解析文件: " + f.getPath());
            csvToEsBulk(csvToEsDTO);
        }
    }

    @Override
    @Async
    public void csvDeepFoldToEs(CsvToEsDTO csvToEsDTO) throws IOException {
        String path = csvToEsDTO.getCsvPath();
        Set<File> files = dataTransUtils.listAllFiles(path);
        for (File f : files) {
            csvToEsDTO.setCsvPath(f.getPath());
            log.info("开始解析文件: " + f.getPath());
            csvToEsBulk(csvToEsDTO);
        }
    }

    @Override
    public void csvToEsBulk(CsvToEsDTO csvToEsDTO) throws IOException {
        //初始化client
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        //判断index是否存在
        GetIndexRequest request = new GetIndexRequest(csvToEsDTO.getIndexName());
        try {
            if (!elasticClient.indices().exists(request, RequestOptions.DEFAULT)) {
                throw new RuntimeException("请检查index配置");
            }
        } catch (IOException e) {
            throw new RuntimeException("无法确认index是否存在",e);
        }
        elasticClient.close();
        csvToEsBulkHandle(csvToEsDTO);
    }

    @Async
    public void csvToEsBulkHandle (CsvToEsDTO csvToEsDTO) throws IOException {
        //初始化client
        RestHighLevelClient elasticClient = elasticClientUtils.getElasticClient();
        String id = UUID.randomUUID().toString();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setId(id);
        taskInfo.setTaskName(csvToEsDTO.getCsvPath());
        taskInfo.setSplit(csvToEsDTO.getSplitWord());
        taskInfo.setTaskState(0);
        taskInfo.setTaskStateName("解析中");
        taskInfo.setTaskType(csvToEsDTO.getIndexName());
        taskInfo.setTitle(String.join(csvToEsDTO.getSplitWord(),csvToEsDTO.getTitle()));
        taskInfo.setStartTime(System.currentTimeMillis());
        taskInfo.setIsCustomTitle(csvToEsDTO.getIsCustomTitle() ? 1 : 0);
        taskInfo.setIsTitleHasCh(csvToEsDTO.getIsTitleHasCh() ? 1 : 0);
        taskInfo.setIsHasTitle(csvToEsDTO.getIsHasTitle() ? 1 : 0);
        //读取csv文件
        try {
            File file = new File(csvToEsDTO.getCsvPath());
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = IoUtil.readBytes(fis);
            taskInfoMapper.insert(taskInfo);
            char splitChar;
            if ( csvToEsDTO.getSplitWord().equals("\\t")) {
                splitChar = '\t';
            } else {
                splitChar = csvToEsDTO.getSplitWord().charAt(0);
            }
            CsvReader csvReader = new CsvReader(csvToEsDTO.getCsvPath(), splitChar,
                    Charset.forName(FileEncodeUtil.getJavaEncode(buffer)));
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
                List<String> mlist = new ArrayList<>(Arrays.asList(oriHeader));

                if (mlist.contains("")) {
                    mlist.remove("");
                    oriHeader = mlist.toArray(new String[0]);
                }

                if (csvToEsDTO.getIsTitleHasCh()) {
                    /* hutao 修改 */
                    List<FieldName> enNames = fieldNameMapper.getEnNames(mlist); // 用mlist所有中文名称去查到field_name的结果
                    List<String> headersNew = new ArrayList<>(); // 新建用来存放查到field_name的英文名称

                    for (FieldName fieldName : enNames){
                        String chName = fieldName.getChName();
                        headersNew.add(fieldName.getEnName()); // 增加查到的中文名称
                        mlist.remove(chName); // 从mlist中删掉查到的中文名称，没被删掉的就是查不到的
                    }
                    if(mlist.size()>0){
                        String[] neworiHeader = mlist.toArray(new String[mlist.size()]);// 没被删掉的就要用来中文转拼音
                        headersNew.addAll(Arrays.asList(dataTransUtils.ch2py(neworiHeader))); // 增加到新建存放英文名称的集合
                    }
                    headers = headersNew.toArray(new String[headersNew.size()]); // 把新建存放英文名称的集合赋值给headers
                    /* hutao 修改 */

//                    headers = dataTransUtils.ch2py(oriHeader);
                } else {
                    headers = oriHeader;
                }
                csvReader.setHeaders(headers);
            }
            if (headers.length < 1) {
                taskInfo.setTaskStateName("解析出错");
                taskInfo.setTaskState(2);
                taskInfoMapper.updateByPrimaryKey(taskInfo);
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
                    //log.info("缓存数据达到bulkSize阈值:{} 开始本次提交", bulkRequest.numberOfActions());
                    BulkResponse bulkResponse = elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                    //log.info("es同步数据结果是否出错:{}", bulkResponse.hasFailures());
                    //log.info("es一共同步数据数量:{}", total);
                    bulkRequest = new BulkRequest();
                }
            }
            //最后提交
            //log.info("提交最后一批数据:{} ", bulkRequest.numberOfActions());
            total = total + bulkRequest.numberOfActions();
            BulkResponse bulkResponse = elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            //log.info("es同步数据结果是否出错:{}", bulkResponse.hasFailures());
            long endTimeMillis = System.currentTimeMillis();
            log.info("es本次同步数据数量:{}", total);
            log.info("es同步数据耗时:{} ms", endTimeMillis - startTimeMillis);
            taskInfo.setTaskState(1);
            taskInfo.setTaskStateName("解析完成");
            taskInfo.setFinishTime(System.currentTimeMillis());
            taskInfoMapper.updateByPrimaryKey(taskInfo);

        } catch (IOException e) {
            taskInfo.setTaskStateName("解析出错");
            taskInfo.setTaskState(2);
            taskInfoMapper.updateByPrimaryKey(taskInfo);
            log.error("解析csv文件失败", e);
            throw new RuntimeException("解析csv文件失败",e);
        }
        elasticClient.close();
    }

    @Override
    public String[] csvLine(String filePath, String splitWord) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String firstLine = reader.readLine();
        return firstLine.split(splitWord);
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

    public static void main(String[] args) {
//        File file = new File("C:\\Users\\apart\\Desktop\\300 rds_2021-03-13_18-33-45-104.dat.txt");
        File file = new File("C:\\Users\\JH\\Desktop\\task_info.sql");
        try(FileInputStream fs = new FileInputStream(file)){
            byte[] buffer = IoUtil.readBytes(fs);
            System.out.println(FileEncodeUtil.getJavaEncode(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
