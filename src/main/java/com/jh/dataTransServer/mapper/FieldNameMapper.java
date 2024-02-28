package com.jh.dataTransServer.mapper;

import com.jh.dataTransServer.common.pojo.FieldName;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liqijian
 */
@Repository
public interface FieldNameMapper extends Mapper<FieldName> {

    @Select("select * from field_name order by create_time desc")
    List<FieldName> getAllFields(int pageNum, int pageSize);

    @Insert("<script>" +
            "INSERT INTO field_name (ch_name, en_name, field_type, notes) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.chName}, #{item.enName}, #{item.fieldType}, #{item.notes})" +
            "</foreach>" +
            "</script>")
    void batchInsert(List<FieldName> userList);

    @Select({
            "<script>",
            "SELECT ch_name,en_name FROM field_name",
            "WHERE ch_name IN",
            "<foreach item='name' collection='names' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach>",
            "</script>"
    })

    List<FieldName> getEnNames(@Param("names") List<String> names);

}
