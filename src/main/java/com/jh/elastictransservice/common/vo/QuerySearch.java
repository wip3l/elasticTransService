package com.jh.elastictransservice.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author liqijian
 */
@Data
@ApiModel("搜索参数实体类")
public class QuerySearch implements Serializable {
    private static final long serialVersionUID = 1954301172787123215L;
    @NotBlank(message = "索引名称不能为空")
    @ApiModelProperty(value = "索引名称", required = true)
    String indexName;
    @ApiModelProperty(value = "关键字（仅用于全文搜素）")
    String keyWord;
    @ApiModelProperty(value = "关键字等值条件\n" +
            "若是一个字符串以%结尾，则匹配以去掉%的字符串开头的记录\n" +
            "若是一个字符串以*开头或结尾，则模糊匹配去掉*的记录  类似于sql中的like '%str%'\n" +
            "若传入的是一个普通的字符串，则等值查询\n" +
            "若传入的是一个集合，则使用的是in条件查询")
    Map<String, Object> equalsCondition;
    @ApiModelProperty(value = "条件范围查询\n" +
            "字段，字段对应值的区间，区间格式[,]/(,)/[,)/(,]，逗号的左右可以没值")
    Map<String, Object> rangeCondition;
    @ApiModelProperty(value = "排序字段\n" +
            "若是字段以中划线-开头，则使用降序排序，类似于sql中的desc\n" +
            "若正常字段排序，则使用增序排序，类似于sql中的asc")
    List<String> orderBy;
    @ApiModelProperty(value = "分页页码（0为首页）")
    Integer pageNum = 0;
    @ApiModelProperty(value = "分页大小")
    Integer pageSize = 10;
}
