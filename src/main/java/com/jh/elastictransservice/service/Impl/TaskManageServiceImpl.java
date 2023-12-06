package com.jh.elastictransservice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.elastictransservice.common.dto.CsvToEsDTO;
import com.jh.elastictransservice.common.pojo.TaskInfo;
import com.jh.elastictransservice.common.vo.CsvToEs;
import com.jh.elastictransservice.mapper.TaskInfoMapper;
import com.jh.elastictransservice.service.DataTransService;
import com.jh.elastictransservice.service.TaskManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author liqijian
 */
@Service
public class TaskManageServiceImpl implements TaskManageService {
    @Autowired
    private TaskInfoMapper taskInfoMapper;
    @Autowired
    private DataTransService dataTransService;

    @Override
    public PageInfo<TaskInfo> getAllTask(int pageNum, int pageSize){
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<TaskInfo> tasks = taskInfoMapper.getTasks();
        PageInfo<TaskInfo> pageInfo = new PageInfo<>(tasks);
        return pageInfo;
    }

    @Override
    public int deleteTask(String id) {
        return taskInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void rerunTask(String id) throws IOException {
        TaskInfo taskInfo = taskInfoMapper.selectByPrimaryKey(id);
        CsvToEs csvToEs = new CsvToEs();
        csvToEs.setCsvPath(taskInfo.getTaskName());
        csvToEs.setTitle(taskInfo.getTitle().split(taskInfo.getSplit()));
        csvToEs.setIndexName(taskInfo.getTaskType());
        csvToEs.setSplitWord(taskInfo.getSplit());
        csvToEs.setIsHasTitle(taskInfo.getIsHasTitle());
        csvToEs.setIsCustomTitle(taskInfo.getIsCustomTitle());
        csvToEs.setIsTitleHasCh(taskInfo.getIsTitleHasCh());
        dataTransService.csvToEsBulk(new CsvToEsDTO(csvToEs));
    }

}
