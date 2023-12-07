package com.jh.elastictransservice.controller;

import com.jh.elastictransservice.result.ExceptionMsg;
import com.jh.elastictransservice.result.ResponseData;
import com.jh.elastictransservice.service.IndexHandleService;
import com.jh.elastictransservice.common.dto.IndexCreateDTO;
import com.jh.elastictransservice.common.vo.IndexCreate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author liqijian
 */
@RestController
@RequestMapping("index")
@Api(tags = "索引操作")
public class IndexHandleController {
    @Autowired
    private IndexHandleService indexHandleService;

    @CrossOrigin
    @ApiOperation(value = "创建索引", notes = "创建索引")
    @PostMapping("/create")
    public ResponseData createIndex(@ApiParam("索引参数") @RequestBody @Valid IndexCreate index) throws IOException {
        CreateIndexResponse response = indexHandleService.createIndex(new IndexCreateDTO(index));
        return new ResponseData(response.isAcknowledged()? ExceptionMsg.SUCCESS: ExceptionMsg.ERROR);
    }

    @CrossOrigin
    @ApiOperation(value = "获取所有索引", notes = "获取所有索引")
    @GetMapping("/listAllIndices")
    public ResponseData listAllIndices() throws IOException {
        GetIndexResponse getIndexResponse = indexHandleService.listAllIndex();
        return new ResponseData(getIndexResponse.getIndices(),ExceptionMsg.SUCCESS);
    }

    @CrossOrigin
    @ApiOperation(value = "获取索引Mapping", notes = "获取索引Mapping")
    @PostMapping("/listMapping")
    public ResponseData listMapping (@ApiParam("索引名称") @RequestParam String indexName) throws IOException {
        GetIndexResponse indexMapping = indexHandleService.getIndexMapping(indexName);
        return new ResponseData(indexMapping.getMappings(),ExceptionMsg.SUCCESS);
    }

    @CrossOrigin
    @ApiOperation(value = "数据统计", notes = "数据统计")
    @PostMapping("/docStatic")
    public ResponseData docStatic () throws IOException {
        HashMap<Object, Object> objectObjectHashMap = indexHandleService.docStatic();
        return new ResponseData(objectObjectHashMap,ExceptionMsg.SUCCESS);
    }

    @CrossOrigin
    @ApiOperation(value = "删除索引", notes = "删除索引")
    @DeleteMapping("/delete")
    public ResponseData deleteIndex(@ApiParam("索引名称") @RequestParam String indexName) throws IOException {
        return new ResponseData(indexHandleService.deleteIndex(indexName).isAcknowledged()? ExceptionMsg.SUCCESS: ExceptionMsg.ERROR);
    }

    @CrossOrigin
    @ApiOperation(value = "清空索引数据", notes = "清空索引数据")
    @PostMapping("/truncate")
    public ResponseData truncateIndex(@ApiParam("索引名称") @RequestParam String indexName) throws IOException {
        indexHandleService.deleteAll(indexName);
        return new ResponseData(ExceptionMsg.SUCCESS.getCode(),"正在清空索引数据： " + indexName);
    }

}
