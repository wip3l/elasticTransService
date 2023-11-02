package com.jh.elastictransservice.utils.dto;

import com.jh.elastictransservice.utils.vo.CsvToEs;
import lombok.Data;

/**
 * @author liqijian
 */
@Data
public class CsvToEsDTO {
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
