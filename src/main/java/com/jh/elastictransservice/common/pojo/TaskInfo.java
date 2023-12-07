package com.jh.elastictransservice.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liqijian
 */
@Data
@Table(name = "task_info")
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = -4861414010338163524L;

    @Id
    private String id;
    private String taskName;
    private String taskType;
    //0解析中；1解析完成；2解析失败
    private Integer taskState;
    private String taskStateName;
    private String split;
    private Integer isHasTitle;
    private Integer isTitleHasCh;
    private Integer isCustomTitle;
    private String title;
    private Long startTime;
    private Long finishTime;

}
