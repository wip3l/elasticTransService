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
    private String taskState;
    private Date startTime;
    private Date finishTime;
    public TaskInfo(String id, String taskName, String taskType, String taskState, Date startTime) {
        this.id = id;
        this.taskName = taskName;
        this.taskType = taskType;
        this.taskState = taskState;
        this.startTime = startTime;
    }

}
