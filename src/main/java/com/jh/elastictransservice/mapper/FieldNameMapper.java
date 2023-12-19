package com.jh.elastictransservice.mapper;

import com.jh.elastictransservice.common.pojo.FieldName;
import org.apache.ibatis.annotations.Insert;
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
    List<FieldName> getAllFields(int pageNum, int pageSize);

    @Insert("<script>" +
            "INSERT INTO field_name (ch_name, en_name, field_type, notes) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.chName}, #{item.enName}, #{item.fieldType}, #{item.notes})" +
            "</foreach>" +
            "</script>")
    void batchInsert(List<FieldName> userList);

}
