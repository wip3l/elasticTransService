package com.jh.dataTransServer.mapper;

import com.jh.dataTransServer.common.pojo.TaskInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liqijian
 */
@Repository
public interface TaskInfoMapper extends Mapper<TaskInfo> {


    @Select("select * from task_info order by start_time desc")
    List<TaskInfo> getTasks();

    @Update("update from task_info set task_state = #{taskState} and task_state_name = #{taskStateName} where id = #{id}")
    void changeState(@Param("taskState") int taskState,@Param("taskStateName") int taskStateName,@Param("id") String id);
}
