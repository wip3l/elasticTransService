package com.jh.elastictransservice.controller;

import com.jh.elastictransservice.result.ResponseData;
import com.jh.elastictransservice.service.DataTransService;
import com.jh.elastictransservice.utils.dto.CsvToEsDTO;
import com.jh.elastictransservice.utils.vo.CsvToEs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author liqijian
 */
@RestController
@RequestMapping("dataTrans")
@Api(tags = "数据解析")
public class DataTransController {
    @Autowired
    private DataTransService dataTransService;

    @PostMapping("/csvTrans")
    @ApiOperation(value = "单个csv文件解析", notes = "单个csv文件解析")
    public ResponseData csvTrans (@ApiParam("csv导入es参数") @RequestBody CsvToEs csvToEs) {
        dataTransService.csvToEs(new CsvToEsDTO(csvToEs));
        return new ResponseData(200,"正在解析");
    }

    @PostMapping("/csvTransBulk")
    @ApiOperation(value = "单个csv文件解析的bulk版本", notes = "单个csv文件解析的bulk版本")
    public ResponseData csvTransBulk (@ApiParam("csv导入es参数") @RequestBody CsvToEs csvToEs) {
        dataTransService.csvToEsBulk(new CsvToEsDTO(csvToEs));
        return new ResponseData(200,"正在解析");
    }

    @PostMapping("/csvFoldToEs")
    @ApiOperation(value = "解析文件该级目录下的所有文件", notes = "解析文件该级目录下的所有文件")
    public ResponseData csvFoldToEs (@ApiParam("csv导入es参数") @RequestBody CsvToEs csvToEs) {
        dataTransService.csvFoldToEs(new CsvToEsDTO(csvToEs));
        return new ResponseData(200,"正在解析");
    }

    @PostMapping("/csvDeepFoldToEs")
    @ApiOperation(value = "遍历该目录到最底层的所有文件并解析", notes = "遍历该目录到最底层的所有文件并解析")
    public ResponseData csvDeepFoldToEs (@ApiParam("csv导入es参数") @RequestBody CsvToEs csvToEs) {
        dataTransService.csvDeepFoldToEs(new CsvToEsDTO(csvToEs));
        return new ResponseData(200,"正在解析");
    }

    @DeleteMapping("/deleteRow")
    @ApiOperation(value = "删除单行数据", notes = "删除单行数据")
    public ResponseData deleteRow (@ApiParam("索引名称") @RequestParam String indexName,
                                   @ApiParam("数据id") @RequestParam String id) throws IOException {
        dataTransService.deleteRow(indexName, id);
        return new ResponseData();
    }

    @PostMapping("/newCsv")
    @ApiOperation(value = "新建示例csv文件", notes = "新建示例csv文件")
    public ResponseData newCsv (@ApiParam("文件名称") @RequestParam String fileName) throws IOException {
        dataTransService.newCsv(fileName);
        return new ResponseData();
    }


}
