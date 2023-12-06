package com.jh.elastictransservice.mapper;

import com.jh.elastictransservice.common.pojo.TaskInfo;
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

    @Update("update from task_info set taskState = #{taskState} where id = #{id}")
    void changeState(@Param("taskState") String taskState,@Param("id") String id);
}
