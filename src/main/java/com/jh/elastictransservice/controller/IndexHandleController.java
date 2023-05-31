package com.jh.elastictransservice.controller;

import com.jh.elastictransservice.utils.result.ResponseData;
import com.jh.elastictransservice.service.IndexHandleService;
import com.jh.elastictransservice.utils.dto.IndexCreateDTO;
import com.jh.elastictransservice.utils.vo.IndexCreate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author liqijian
 */
@RestController
@RequestMapping("index")
@Api(tags = "索引操作")
public class IndexHandleController {
    @Autowired
    private IndexHandleService indexHandleService;

    @ApiOperation(value = "创建索引", notes = "创建索引")
    @PostMapping("/create")
    public ResponseData createIndex(@ApiParam("索引参数") @RequestBody @Valid IndexCreate index) throws IOException {
        CreateIndexResponse response = indexHandleService.createIndex(new IndexCreateDTO(index));
        return new ResponseData(response.isAcknowledged()? 200: 500);
    }

    @ApiOperation(value = "删除索引", notes = "删除索引")
    @DeleteMapping("/delete")
    public ResponseData deleteIndex(@ApiParam("索引名称") @RequestParam String indexName) throws IOException {
        return new ResponseData(indexHandleService.deleteIndex(indexName).isAcknowledged()? 200: 500);
    }

    @ApiOperation(value = "清空索引数据", notes = "清空索引数据")
    @PostMapping("/truncate")
    public ResponseData truncateIndex(@ApiParam("索引名称") @RequestParam String indexName) throws IOException {
        indexHandleService.deleteAll(indexName);
        return new ResponseData();
    }
}
