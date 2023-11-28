package com.jh.elastictransservice.controller;

import com.jh.elastictransservice.result.ResponseData;
import com.jh.elastictransservice.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author: Hutao
 * @CreateDate: 2023/11/27 15:38
 */

@Controller
@ResponseBody
@RequestMapping("/hdfs")
public class HdfsController {
    @Autowired
    HdfsService hdfsService;

    @RequestMapping("/getHdfsCatalog")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "获取HDFS文件地址和目录")
    public ResponseData getHdfsCatalog(@RequestParam(required=false, defaultValue = "/") String path)throws Exception{

        Map<String, Object> stringObjectMap =  hdfsService.getHdfsCatalog(path);
        return new ResponseData(stringObjectMap);
    }

}
