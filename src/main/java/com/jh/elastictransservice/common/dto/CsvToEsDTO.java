package com.jh.elastictransservice.common.dto;

import com.jh.elastictransservice.common.vo.CsvToEs;
import lombok.Data;

/**
 * @author liqijian
 */
@Data
public class CsvToEsDTO {
    Long taskId;
    String indexName;
    String csvPath;
    String splitWord;
    Boolean isHasTitle;
    Boolean isTitleHasCh;
    Boolean isCustomTitle;
    String[] title;
    public CsvToEsDTO (CsvToEs csvToEs) {
        this.indexName = csvToEs.getIndexName();
        this.csvPath = csvToEs.getCsvPath();
        this.splitWord = csvToEs.getSplitWord();
        this.isHasTitle = csvToEs.getIsHasTitle();
        this.isTitleHasCh = csvToEs.getIsTitleHasCh();
        this.isCustomTitle = csvToEs.getIsCustomTitle();
        this.title = csvToEs.getTitle();
    }
}
