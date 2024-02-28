package com.jh.dataTransServer.controller;

import com.jh.dataTransServer.result.ExceptionMsg;
import com.jh.dataTransServer.result.ResponseData;
import com.jh.dataTransServer.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseData(res, ExceptionMsg.SUCCESS);
    }

    @CrossOrigin
    @GetMapping("/getHdfsFilePlay")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "查看HDFS文件")
    public ResponseEntity<InputStreamResource> getHdfsFilePlay(@RequestParam(required=true) String path)throws Exception{
        return hdfsService.getHdfsFilePlay(path);
    }

    @CrossOrigin
    @GetMapping("/uploadFilesToHdfs")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "上传HDFS文件")
    public ResponseData uploadFilesToHdfs(
            @RequestParam(required=true) String path,
            @RequestParam(required=true) String localFolderPath
    )throws Exception{
        Map<String ,Object> res =  hdfsService.uploadFilesToHdfs(path, localFolderPath);
        return new ResponseData(res,ExceptionMsg.SUCCESS);
    }

    @CrossOrigin
    @GetMapping("/createHdfsDirectory")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "创建HDFS目录")
    public ResponseData createHdfsDirectory(@RequestParam(required=true) String path)throws Exception{
        Map<String ,Object> res =  hdfsService.createHdfsDirectory(path);
        return new ResponseData(res,ExceptionMsg.SUCCESS);
    }
    @CrossOrigin
    @GetMapping("/deleteHdfsPath")
    @ApiOperation(value = "HDFS文件地址和目录", notes = "删除HDFS目录或文件")
    public ResponseData deleteHdfsPath(@RequestParam(required=true) String path)throws Exception{
        Map<String ,Object> res =  hdfsService.deleteHdfsPath(path);
        return new ResponseData(res,ExceptionMsg.SUCCESS);
    }

}
