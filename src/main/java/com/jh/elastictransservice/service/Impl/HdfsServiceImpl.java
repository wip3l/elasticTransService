package com.jh.elastictransservice.service.Impl;

import com.jh.elastictransservice.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hutao
 * @CreateDate: 2023/11/27 15:41
 */

@Service
public class HdfsServiceImpl implements HdfsService {

    @Value("${hdfs.address}")
    private String hdfsAddress;
    @Value("${hdfs.web}")
    private String hdfsWeb;

    @Override
    public Map<String, Object> getHdfsCatalog(String path) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsAddress);
        FileSystem fs = FileSystem.get(conf);
        Path directoryPath = new Path(path);
        FileStatus[] fileStatuses = fs.listStatus(directoryPath);

        for (FileStatus fileStatus : fileStatuses) {
            Map<String, Object> map2 = new HashMap<>();
            String docPath = fileStatus.getPath().toUri().getPath();
            String downLoadPath = fileStatus.getPath().toUri().getPath();
            String docName = fileStatus.getPath().getName();
            boolean isFile = fileStatus.isFile();
            map2.put("downLoadPath",hdfsWeb+docPath+"?op=OPEN");
            map2.put("docPath",docPath);
            map2.put("docName",docName);
            map2.put("isFile",isFile);
            map.put(docName, map2);
        }

        fs.close();
        return map;
    }
}
