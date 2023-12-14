package com.jh.elastictransservice.service.Impl;

import cn.hutool.core.io.IoUtil;
import com.jh.elastictransservice.service.HdfsService;
import com.jh.elastictransservice.utils.FileEncodeUtil;
import com.jh.elastictransservice.utils.HdfsConn;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hutao
 * @CreateDate: 2023/11/27 15:41
 */

@Service
public class HdfsServiceImpl implements HdfsService {

    @Autowired
    HdfsConn hdfsConn;

    @Value("${hdfs.web}")
    private String hdfsWeb;

    @Override
    public Map<String, Object> getHdfsCatalog(String path) throws Exception {
        Map<String, Object> map = new HashMap<>();
        FileSystem fs = hdfsConn.getFileSystem();
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
    @Override
    public Map<String ,Object> getHdfsCatalogList(String path,int pageNum,int pageSize) throws Exception {
        Map<String ,Object> resMap = new HashMap<>();
        List<Object> list = new ArrayList<>();
        FileSystem fs = hdfsConn.getFileSystem();
        Path directoryPath = new Path(path);
        FileStatus[] fileStatuses = fs.listStatus(directoryPath);
        int totalNum = fileStatuses.length;

        // 计算起始位置和结束位置
        int start = (pageNum - 1) * pageSize;
        int end = start + pageSize;

        for (int i = start; i < end && i < totalNum; i++) {
            FileStatus fileStatus = fileStatuses[i];

            Map<String, Object> map2 = new HashMap<>();
            String docPath = fileStatus.getPath().toUri().getPath();
            String downLoadPath = fileStatus.getPath().toUri().getPath();
            String docName = fileStatus.getPath().getName();
            boolean isFile = fileStatus.isFile();
            map2.put("downLoadPath",hdfsWeb+docPath+"?op=OPEN");
            map2.put("docPath",docPath);
            map2.put("docName",docName);
            map2.put("isFile",isFile);
            list.add(map2);
        }

        resMap.put("totalNum", totalNum);
        resMap.put("pageList", list);
        resMap.put("pageNum", pageNum);
        resMap.put("pageSize", pageSize);
        fs.close();
        return resMap;
    }

    @Override
    public ResponseEntity<InputStreamResource> getHdfsFilePlay(String path) throws Exception {
        FileSystem fileSystem = hdfsConn.getFileSystem();

        Path hdfsPath = new Path(URLDecoder.decode(path, "UTF-8"));
        if (!fileSystem.exists(hdfsPath)) {
            return ResponseEntity.notFound().build();
        }

        FSDataInputStream inputStream = fileSystem.open(hdfsPath);
        FSDataInputStream is = fileSystem.open(hdfsPath);
        HttpHeaders headers = new HttpHeaders();

        String filename = hdfsPath.getName();
        String fileType = filename.substring(filename.lastIndexOf(".")).toLowerCase();

        switch (fileType){
            case ".txt":
                byte[] buffer = IoUtil.readBytes(is);
                MediaType mediaType = new MediaType("text", "plain", Charset.forName(FileEncodeUtil.getJavaEncode(buffer)));
                headers.setContentType(mediaType);
                break;
            case ".html":
                headers.setContentType(MediaType.parseMediaType("text/html"));
                break;
            case ".mp4":
                headers.setContentType(MediaType.parseMediaType("video/mp4"));
                break;
            case ".quicktime":
                headers.setContentType(MediaType.parseMediaType("video/quicktime"));
                break;
            case ".x-msvideo":
                headers.setContentType(MediaType.parseMediaType("video/x-msvideo"));
                break;
            case ".x-flv":
                headers.setContentType(MediaType.parseMediaType("video/x-flv"));
                break;
            case ".jpeg":
                headers.setContentType(MediaType.parseMediaType("image/jpeg"));
                break;
            case ".jpg":
                headers.setContentType(MediaType.parseMediaType("image/jpeg"));
                break;
            case ".png":
                headers.setContentType(MediaType.parseMediaType("image/png"));
                break;
            case ".gif":
                headers.setContentType(MediaType.parseMediaType("image/gif"));
                break;
            case ".svg":
                headers.setContentType(MediaType.parseMediaType("image/svg+xml"));
                break;
            case ".mpeg":
                headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
                break;
            case ".mp3":
                headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
                break;
            case ".wav":
                headers.setContentType(MediaType.parseMediaType("audio/wav"));
                break;
            case ".ogg":
                headers.setContentType(MediaType.parseMediaType("audio/ogg"));
                break;
            case ".aac":
                headers.setContentType(MediaType.parseMediaType("audio/aac"));
                break;
            case ".pdf":
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                break;
            default:
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(hdfsPath.getName(), "UTF-8") + "\""); // 编码中文文件名
                break;
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(inputStream));
    }

    @Override
    public Map<String ,Object> uploadFilesToHdfs(String path, String localFolderPath) throws Exception {
        Map<String ,Object> resMap = new HashMap<>();
        FileSystem fileSystem = hdfsConn.getFileSystem();

        String filesPath = URLDecoder.decode(localFolderPath, "UTF-8");
        File localFolder = new File(localFolderPath);
        if (localFolder.isDirectory()) {
//            File[] files = localFolder.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isFile()) {
//                        Path hdfsFilePath = new Path(path + "/" + file.getName());
//                        fileSystem.copyFromLocalFile(new Path(file.getAbsolutePath()), hdfsFilePath);
//                    }
//                }
//            }

            uploadFolderContents(fileSystem, localFolder, path);
            resMap.put("res","上传成功");
            fileSystem.close();
        }else{
            Path localPath = new Path(filesPath);
            Path hdfsPath = new Path(path + "/" + localPath.getName());
            fileSystem.copyFromLocalFile(localPath, hdfsPath);
            fileSystem.close();
            resMap.put("res","上传成功");
        }

        return resMap;
    }

    @Override
    public Map<String, Object> createHdfsDirectory(String path) throws Exception {
        Map<String ,Object> resMap = new HashMap<>();
        FileSystem fileSystem = hdfsConn.getFileSystem();
        Path hdfsDirectory = new Path(path);
        if (!fileSystem.exists(hdfsDirectory)) {
            fileSystem.mkdirs(hdfsDirectory);
            fileSystem.close();
            resMap.put("res","创建成功");
        } else {
            resMap.put("res","目录已存在");
        }
        return resMap;
    }

    @Override
    public Map<String, Object> deleteHdfsPath(String path) throws Exception {
        Map<String ,Object> resMap = new HashMap<>();
        FileSystem fs = hdfsConn.getFileSystem();

        Path hdfsPath = new Path(path);
        if (fs.exists(hdfsPath)) {
            if (fs.isDirectory(hdfsPath)) {
                fs.delete(hdfsPath, true); // 删除文件夹
            } else {
                fs.delete(hdfsPath, false); // 删除文件
            }
            fs.close();
            resMap.put("res","删除成功");
        } else {
            resMap.put("res","文件或目录不存在");
        }

        return resMap;
    }

    private static void uploadFolderContents(FileSystem fs, File folder, String hdfsPath) throws IOException {
        Map<String ,Object> resMap = new HashMap<>();

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Path hdfsFilePath = new Path(hdfsPath + "/" + file.getName());
                    fs.copyFromLocalFile(new Path(file.getAbsolutePath()), hdfsFilePath);
                } else if (file.isDirectory()) {
                    String newHdfsPath = hdfsPath + "/" + file.getName();
                    fs.mkdirs(new Path(newHdfsPath));
                    uploadFolderContents(fs, file, newHdfsPath);
                }
            }
        }

    }

}
