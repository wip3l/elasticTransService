package com.jh.elastictransservice.controller;

import com.jh.elastictransservice.result.ResponseData;
import com.jh.elastictransservice.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @CrossOrigin
    @GetMapping("/getHdfsCatalog")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "获取HDFS文件地址和目录")
    public ResponseData getHdfsCatalog(
            @RequestParam(required=false, defaultValue = "/") String path,
            @RequestParam(required=false, defaultValue = "1") int pageNum,
            @RequestParam(required=false, defaultValue = "100") int pageSize
    )throws Exception{

//        Map<String, Object> res =  hdfsService.getHdfsCatalog(path);
        Map<String ,Object> res =  hdfsService.getHdfsCatalogList(path,pageNum,pageSize);
        return new ResponseData(res);
    }

    @CrossOrigin
    @GetMapping("/getHdfsFilePlay")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "获取HDFS文件地址和目录")
    public ResponseEntity<InputStreamResource> getHdfsFilePlay(@RequestParam(required=true) String path)throws Exception{
        return hdfsService.getHdfsFilePlay(path);
    }

    @CrossOrigin
    @GetMapping("/uploadFilesToHdfs")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "获取HDFS文件地址和目录")
    public ResponseData uploadFilesToHdfs(
            @RequestParam(required=false, defaultValue = "/") String path,
            @RequestParam(required=true) MultipartFile[] files
    )throws Exception{
        Map<String ,Object> res =  hdfsService.uploadFilesToHdfs(path,files);
        return new ResponseData(res);
    }



}
