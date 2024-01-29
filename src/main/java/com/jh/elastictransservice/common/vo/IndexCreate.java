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
@ApiModel("索引创建参数实体类")
public class IndexCreate implements Serializable {
    private static final long serialVersionUID = 6888849977549774413L;
    @NotBlank(message = "索引名称不能为空")
    @ApiModelProperty(value = "索引名称", required = true)
    private String indexName;
    @ApiModelProperty(value = "索引数据")
    private String showName;
    @ApiModelProperty(value = "索引用于展示的中文名称")
    private List<Map<String, String>> mapping;
}
