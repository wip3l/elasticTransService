package com.jh.dataTransServer.common.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liqijian
 */
@Data
@Table(name = "index_name")
public class IndexName implements Serializable {
    private static final long serialVersionUID = -4861414010338163524L;

    @Id
    private String id;
    private String index;
    private String showName;
    private Date createTime;
}
