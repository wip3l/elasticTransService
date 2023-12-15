package com.jh.elastictransservice.common.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liqijian
 */
@Data
@Table(name = "field_name")
public class FieldName implements Serializable {

    private static final long serialVersionUID = -4861414010338163524L;

    @Id
    private String id;
    private String index;
    private String title;
    private String field_name;
    private String field_type;
    private Date create_time;
}
