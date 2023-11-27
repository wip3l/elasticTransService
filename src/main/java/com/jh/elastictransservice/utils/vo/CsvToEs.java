package com.jh.elastictransservice.utils.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author liqijian
 */
@Data
@ApiModel("csv导入es参数实体类")
public class CsvToEs implements Serializable {
    private static final long serialVersionUID = -1930415251596038147L;
    @NotBlank(message = "索引名不能为空")
    @ApiModelProperty(value = "索引名", required = true)
    String indexName;
    @NotBlank(message = "csv文件路径不能为空")
    @ApiModelProperty(value = "csv文件路径", required = true)
    String csvPath;
    @NotBlank(message = "分隔符不能为空")
    @ApiModelProperty(value = "分隔符", required = true)
    String splitWord;
    @ApiModelProperty(value = "csv文件首行是否有标题")
    Boolean isHasTitle;
    @ApiModelProperty(value = "是否开启表头汉字转拼音")
    Boolean isTitleHasCh;
    @NotNull(message = "是否自定义表头不能为空")
    @ApiModelProperty(value = "是否自定义表头", required = true)
    Boolean isCustomTitle;
    @ApiModelProperty(value = "自定义文件头内容")
    String[] title;
}
