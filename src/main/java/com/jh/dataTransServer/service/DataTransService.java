package com.jh.dataTransServer.service;

import com.jh.dataTransServer.common.dto.CsvToEsDTO;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

/**
 * @author liqijian
 */
public interface DataTransService {

    void newCsv(String fileName) throws IOException;

    @Async
    void csvFoldToEs(CsvToEsDTO csvToEsDTO) throws IOException;

    @Async
    void csvDeepFoldToEs(CsvToEsDTO csvToEsDTO) throws IOException;

    void csvToEsBulk(CsvToEsDTO csvToEsDTO) throws IOException;

    String[] csvLine(String filePath, String splitWord) throws IOException;

    void deleteRow(String indexName, String id) throws IOException;
}
