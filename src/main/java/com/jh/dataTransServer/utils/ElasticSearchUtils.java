package com.jh.dataTransServer.utils;

import com.jh.dataTransServer.config.DefineConstant;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author liqijian
 * @Description ElasticSearch搜索工具类
 */
@Component
public class ElasticSearchUtils {
    @Autowired
    private ElasticClientUtils elasticClientUtils;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * @Description ElasticSearch条件查询
     * @param indexName       (索引名)也可以类似的说成表名
     * @param equalsCondition 关键字等值条件
     *                        若是一个字符串以%结尾，则匹配以去掉%的字符串开头的记录
     *                        若是一个字符串以*开头或结尾，则模糊匹配去掉*的记录  类似于sql中的like '%str%'
     *                        若传入的是一个普通的字符串，则等值查询
     *                        若传入的是一个集合，则使用的是in条件查询
     * @param rangeCondition  条件范围查询
     *                        字段，字段对应值的区间，区间格式[,]/(,)/[,)/(,]，逗号的左右可以没值
     * @param orderBy         排序字段
     *                        若是字段以中划线-开头，则使用降序排序，类似于sql中的desc
     *                        若正常字段排序，则使用增序排序，类似于sql中的asc
     * @param pageNum         页数
     * @param pageSize        每页大小
     * @return Map<String,Object>
     */
    public Map<String ,Object> queryForEs(String indexName, Map<String, Object> equalsCondition, Map<String, Object> rangeCondition,
                                          List<String> orderBy, int pageNum, int pageSize){
        Map<String, Object> resultMap = new HashMap<>(8);
        List<Map<String,Object>> queryResult = new ArrayList<>();
        long totalNum = 0;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // and 等值查询
        // 某一field=具体的值； 也可以某一field 的值 in 具体定义集合里的值
        if (null != equalsCondition && !equalsCondition.isEmpty()){
            for (Map.Entry<String ,Object> entry : equalsCondition.entrySet()){
                String key = entry.getKey();
                //由于我创建索引的时候使用字符串不分词使用的.keyword类型
                if (key.endsWith("_s")){
                    queryValueBuild(boolQueryBuilder, key + ".keyword", entry.getValue());
                }else{
                    queryValueBuild(boolQueryBuilder, key, entry.getValue());
                }
            }
        }
        //范围查询
        if (null != rangeCondition && !rangeCondition.isEmpty()){
            rangeValueBuild(boolQueryBuilder, rangeCondition);
        }
        sourceBuilder.query(boolQueryBuilder);
        //排序
        if (null != orderBy && !orderBy.isEmpty()){
            buildSort(sourceBuilder, orderBy);
        }
        //分页(es分页查询默认是查询返回10条记录，而深度分页，默认是10000条数据,也就是一次性最多返回10000条,设置size就可以实现，但是如果实际数据量特别大，可以使用scroll游标查询，此处主要常规分页查询)
        if (pageNum > 0){
            sourceBuilder.from(pageSize * (pageNum - 1));
        } else {
            sourceBuilder.from(0);
        }
        sourceBuilder.size(pageSize);

        //执行查询
        SearchResponse response = executeSearch(indexName, sourceBuilder);
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        totalNum = searchHits.getTotalHits().value;
        for (SearchHit hit : hits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            sourceMap.put("id", hit.getId());
            queryResult.add(sourceMap);
        }
        resultMap.put("pageList", queryResult);
        resultMap.put("totalNum", totalNum);
        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        return resultMap;
    }

    /**
     * @Description 查询条件组装
     * @param boolQueryBuilder 查询条件
     * @param key 查询字段
     * @param value 查询字段对应的值
     */
    private static void queryValueBuild(BoolQueryBuilder boolQueryBuilder, String key, Object value){
        TermQueryBuilder termQueryBuilder;
        if (null != value && !"".equals(value)){
            if (value instanceof String){
                String strValue = (String) value;
                if (strValue.endsWith("%")){
                    PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(key,strValue.replace("%",""));
                    boolQueryBuilder.must(prefixQueryBuilder);
                }else if (strValue.startsWith("*") || strValue.endsWith("*")){
                    MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, strValue.replace("*",""));
                    boolQueryBuilder.must(matchQueryBuilder);
                }else {
                    termQueryBuilder = QueryBuilders.termQuery(key, strValue);
                    boolQueryBuilder.must(termQueryBuilder);
                }
            } else if (value instanceof Collection){
                Collection<? extends Object> collectionValue = (Collection<? extends Object>) value;
                //此处使用了多值条件
                boolQueryBuilder.must(QueryBuilders.termsQuery(key, collectionValue));
            } else {
                termQueryBuilder = QueryBuilders.termQuery(key, value);
                boolQueryBuilder.must(termQueryBuilder);
            }
        }
    }

    /**
     * @Description 范围条件查询组装
     * @param boolQueryBuilder 查询条件
     * @param rangeCondition  范围条件
     */
    private void rangeValueBuild(BoolQueryBuilder boolQueryBuilder, Map<String, Object> rangeCondition){
        for (Map.Entry<String, Object> entry : rangeCondition.entrySet()){
            Map<String, Object> range = intervalParse((String) entry.getValue());
            String key = entry.getKey();
            RangeQueryBuilder rangeQueryBuilder;
            if (key.endsWith("_s")){
                rangeQueryBuilder = QueryBuilders.rangeQuery(key + ".keyword");
            }else {
                rangeQueryBuilder = QueryBuilders.rangeQuery(key);
            }
            if (!StringUtils.isEmpty(range.get("leftValue"))){
                if (DefineConstant.INTERVAL_OPEN_VALUE.equals(range.get("leftType"))){
                    rangeQueryBuilder.from(range.get("leftValue"),false);
                } else if (DefineConstant.INTERVAL_CLOSE_VALUE.equals(range.get("leftType"))){
                    rangeQueryBuilder.from(range.get("leftValue"),true);
                }
            }
            if (!StringUtils.isEmpty(range.get("rightValue"))){
                if (DefineConstant.INTERVAL_OPEN_VALUE.equals(range.get("rightType"))){
                    rangeQueryBuilder.to(range.get("rightValue"),false);
                } else if (DefineConstant.INTERVAL_CLOSE_VALUE.equals(range.get("rightType"))){
                    rangeQueryBuilder.to(range.get("rightValue"),true);
                }
            }
            boolQueryBuilder.must(rangeQueryBuilder);
        }
    }

    /**
     * @Description 区间解析：[,]/(,)/[,)/(,]
     * @param interval 区间参数
     * @return Map<String, Object>
     */
    private Map<String, Object> intervalParse(String interval){
        Map<String, Object> range = new HashMap<>();
        if (interval.startsWith(DefineConstant.INTERVAL_CLOSE_LEFT)){
            range.put("leftType", DefineConstant.INTERVAL_CLOSE_VALUE);
        } else if (interval.startsWith(DefineConstant.INTERVAL_OPEN_LEFT)){
            range.put("leftType", DefineConstant.INTERVAL_OPEN_VALUE);
        } else{
            log.error("区间参数格式错误：{}",interval);
            //若实际业务相关需要，抛出异常处理throw new Exception();
        }
        if (interval.endsWith(DefineConstant.INTERVAL_CLOSE_RIGHT)){
            range.put("rightType", DefineConstant.INTERVAL_CLOSE_VALUE);
        } else if (interval.startsWith(DefineConstant.INTERVAL_OPEN_RIGHT)){
            range.put("rightType", DefineConstant.INTERVAL_OPEN_VALUE);
        } else{
            log.error("区间参数格式错误：{}",interval);
            //若实际业务相关需要，抛出异常处理throw new Exception();
        }
        int strLen = interval.length();
        String[] lr = interval.substring(1, strLen - 1).split(DefineConstant.COMMAN_SIGN, 2);
        if (lr.length > 0){
            range.put("leftValue", lr[0]);
        }
        if (lr.length > 1){
            range.put("rightValue", lr[1]);
        }
        return range;
    }

    /**
     * @Description 查询排序
     * @param sourceBuilder 查询条件
     * @param orderBy 排序字段
     */
    private static void buildSort(SearchSourceBuilder sourceBuilder, List<String> orderBy){
        SortBuilder<FieldSortBuilder> sortBuilder;
        for (String sortField : orderBy){
            if (sortField.startsWith("-")){
                //降序排序
                if (sortField.endsWith("_s")){
                    sortBuilder = SortBuilders.fieldSort(sortField.replace("-","") + ".keyword").order(SortOrder.DESC);
                } else {
                    sortBuilder = SortBuilders.fieldSort(sortField.replace("-","")).order(SortOrder.DESC);
                }
            } else {
                //升序排序
                if (sortField.endsWith("_s")){
                    sortBuilder = SortBuilders.fieldSort(sortField.replace("-","") + ".keyword").order(SortOrder.ASC);
                } else {
                    sortBuilder = SortBuilders.fieldSort(sortField.replace("-","")).order(SortOrder.ASC);
                }
            }
            sourceBuilder.sort(sortBuilder);
        }
    }

    /**
     * @Description 执行查询
     * @param tableName 对应的es的index名
     * @param sourceBuilder 查询条件
     * @return SearchResponse
     */
    private  SearchResponse executeSearch(String tableName, SearchSourceBuilder sourceBuilder){
        // 获取不同系统的换行符
        String lineSeparator = System.lineSeparator();
        log.info(lineSeparator + "index:" + tableName + lineSeparator + "search:"+ sourceBuilder.toString() + lineSeparator);
        RestHighLevelClient client = elasticClientUtils.getElasticClient();
        SearchRequest searchRequest = new SearchRequest(tableName);
        SearchResponse response = null;
        searchRequest.source(sourceBuilder);
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("search status:{}, totalNum:{}",response.status(), response.getHits().getTotalHits());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * @Description 关键字查询
     * @param indexName 对应的es的index名
     * @param keyWord 关键词
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Map<String, Object>
     */
    public Map<String ,Object> queryKeyword(String indexName, String keyWord, int pageNum, int pageSize){
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(keyWord);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryStringQueryBuilder);
        if (pageNum > 0){
            searchSourceBuilder.from(pageSize * (pageNum - 1));
        } else {
            searchSourceBuilder.from(0);
        }
        searchSourceBuilder.size(pageSize);
        SearchResponse response = executeSearch(indexName, searchSourceBuilder);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (response != null) {
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                sourceAsMap.put("id", hit.getId());
                resultList.add(sourceAsMap);
            }
            result.put("totalNum", hits.getTotalHits().value);
            result.put("pageList", resultList);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
        }
        return result;
    }

}
