package com.jh.elastictransservice.mapper;

import com.jh.elastictransservice.common.pojo.FieldName;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liqijian
 */
@Repository
public interface FieldNameMapper extends Mapper<FieldName> {

    @Select("select * from field_name order by create_time desc")
    List<FieldName> getFields();

    @Select("select * from field_name  where index = #{index} order by create_time desc")
    List<FieldName> getByIndex(@Param("index") String index);
}
