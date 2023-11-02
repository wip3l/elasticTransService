package com.jh.elastictransservice.service;

import com.jh.elastictransservice.utils.dto.CsvToEsDTO;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

/**
 * @author liqijian
 */
public interface DataTransService {

    void csvToEs(CsvToEsDTO csvToEsDTO);

    void newCsv(String fileName) throws IOException;

    @Async
    void csvFoldToEs(CsvToEsDTO csvToEsDTO);

    @Async
    void csvDeepFoldToEs(CsvToEsDTO csvToEsDTO);

    void csvToEsBulk(CsvToEsDTO csvToEsDTO);

    void deleteRow(String indexName, String id) throws IOException;
}
