package com.jh.dataTransServer.controller;

import com.github.pagehelper.PageInfo;
import com.jh.dataTransServer.common.pojo.TaskInfo;
import com.jh.dataTransServer.result.ResponseData;
import com.jh.dataTransServer.service.TaskManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author liqijian
 */
@RestController
@RequestMapping("task")
@Api(tags = "任务管理")
public class TaskManageController {
    @Autowired
    private TaskManageService taskManageService;

    @CrossOrigin
    @GetMapping("/getAll")
    @ApiOperation(value = "获取任务列表", notes = "获取任务列表")
    public ResponseData getAllTask(@ApiParam("页码") @RequestParam int pageNum,
                                   @ApiParam("分页大小") @RequestParam int pageSize) {

        PageInfo<TaskInfo> allTask = taskManageService.getAllTask(pageNum, pageSize);
        return new ResponseData(allTask);
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除任务", notes = "删除任务")
    public ResponseData deleteTask(@ApiParam("任务ID") @RequestParam String id) {
        int i = taskManageService.deleteTask(id);
        if (i==1) {
            return new ResponseData("删除成功");
        } else {
            return new ResponseData(500,"删除失败");
        }
    }

    @CrossOrigin
    @PostMapping("/rerun")
    @ApiOperation(value = "重启任务", notes = "重启任务")
    public ResponseData rerunTask(@ApiParam("任务ID") @RequestParam String id) throws IOException {
        taskManageService.rerunTask(id);
        return new ResponseData(200,"任务重启成功");
    }
}
