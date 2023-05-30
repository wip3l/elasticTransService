package com.jh.elastictransservice.service;

import com.jh.elastictransservice.utils.dto.CsvToEsDTO;

import java.io.IOException;

/**
 * @author liqijian
 */
public interface DataTransService {

    void csvToEs(CsvToEsDTO csvToEsDTO);

    void newCsv(String fileName) throws IOException;

    void csvToEsBulk(CsvToEsDTO csvToEsDTO);

    void deleteRow(String indexName, String id) throws IOException;
}
