package com.jh.elastictransservice.controller;

import com.github.pagehelper.PageInfo;
import com.jh.elastictransservice.common.pojo.FieldName;
import com.jh.elastictransservice.result.ResponseData;
import com.jh.elastictransservice.service.FieldNameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Hutao
 * @CreateDate: 2023/12/19 14:10
 */

@RestController
@RequestMapping("fieldName")
@Api(tags = "字段名称管理")
public class FieldNameController {

    @Autowired
    private

    FieldNameService fieldNameServe;

    @CrossOrigin
    @GetMapping("/getAllFields")
    @ApiOperation(value = "获取字段列表", notes = "获取字段列表")
    public ResponseData getAllFields(@ApiParam("页码") @RequestParam int pageNum,
                                   @ApiParam("分页大小") @RequestParam int pageSize) {

        PageInfo<FieldName> allFields = fieldNameServe.getAllFields(pageNum, pageSize);
        return new ResponseData(allFields);
    }

    @CrossOrigin
    @PostMapping("/batchInsert")
    @ApiOperation(value = "获取字段列表", notes = "获取字段列表")
    public ResponseData batchInsert(@ApiParam("批量插入json数组") @RequestBody List<FieldName> userList) {
        fieldNameServe.batchInsert(userList);
        return new ResponseData();
    }
}
