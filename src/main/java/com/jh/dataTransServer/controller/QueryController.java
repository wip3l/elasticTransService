package com.jh.dataTransServer.controller;

import com.jh.dataTransServer.result.ExceptionMsg;
import com.jh.dataTransServer.result.ResponseData;
import com.jh.dataTransServer.service.ElasticQuerySearchService;
import com.jh.dataTransServer.common.dto.QuerySearchDTO;
import com.jh.dataTransServer.common.vo.QuerySearch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author liqijian
 */
@RestController
@RequestMapping("query")
@Api(tags = "数据搜索")
public class QueryController {

    @Autowired
    private ElasticQuerySearchService elasticQuerySearchService;

    @CrossOrigin
    @PostMapping("/search")
    @ApiOperation(value = "全文搜索", notes = "全文搜索")
    public ResponseData querySearch (@ApiParam("搜索参数")  @RequestBody QuerySearch querySearch) {
        Map<String, Object> stringObjectMap = elasticQuerySearchService.querySearch(new QuerySearchDTO(querySearch));
        return new ResponseData(stringObjectMap, ExceptionMsg.SUCCESS);
    }

    @CrossOrigin
    @PostMapping("/highSearch")
    @ApiOperation(value = "高级搜索", notes = "高级搜索")
    public ResponseData highQuerySearch (@ApiParam("搜索参数") @RequestBody QuerySearch querySearch) {
        Map<String, Object> stringObjectMap = elasticQuerySearchService.highQuerySearch(new QuerySearchDTO(querySearch));
        return new ResponseData(stringObjectMap,ExceptionMsg.SUCCESS);
    }
}
