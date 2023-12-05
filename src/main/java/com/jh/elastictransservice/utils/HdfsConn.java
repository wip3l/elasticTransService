package com.jh.elastictransservice.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @Author: Hutao    //作者
 * @CreateDate: 2023/11/28 14:09	//创建时间
 */

@Component
public class HdfsConn {

    @Value("${hdfs.address}")
    private String hdfsAddress;

    public FileSystem getFileSystem()throws Exception{
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsAddress);
        return FileSystem.get(conf);
    }
}
