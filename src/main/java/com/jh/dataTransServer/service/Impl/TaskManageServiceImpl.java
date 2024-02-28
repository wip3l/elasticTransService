package com.jh.dataTransServer.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.dataTransServer.common.dto.CsvToEsDTO;
import com.jh.dataTransServer.common.pojo.TaskInfo;
import com.jh.dataTransServer.common.vo.CsvToEs;
import com.jh.dataTransServer.mapper.TaskInfoMapper;
import com.jh.dataTransServer.service.DataTransService;
import com.jh.dataTransServer.service.TaskManageService;
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
        csvToEs.setIsHasTitle(taskInfo.getIsHasTitle().equals(1));
        csvToEs.setIsCustomTitle(taskInfo.getIsCustomTitle().equals(1));
        csvToEs.setIsTitleHasCh(taskInfo.getIsTitleHasCh().equals(1));
        dataTransService.csvToEsBulk(new CsvToEsDTO(csvToEs));
    }

}
