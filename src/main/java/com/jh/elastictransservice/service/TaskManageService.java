package com.jh.elastictransservice.service;

import com.github.pagehelper.PageInfo;
import com.jh.elastictransservice.common.pojo.TaskInfo;

import java.io.IOException;

/**
 * @author liqijian
 */
public interface TaskManageService {
    PageInfo<TaskInfo> getAllTask(int pageNum, int pageSize);

    int deleteTask(String id);

    void rerunTask(String id) throws IOException;
}
