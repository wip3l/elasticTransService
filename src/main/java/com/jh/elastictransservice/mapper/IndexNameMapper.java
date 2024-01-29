package com.jh.elastictransservice.mapper;

import com.jh.elastictransservice.common.pojo.IndexName;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: Hutao
 * @CreateDate: 2024/1/29 13:52
 */
@Repository
public interface IndexNameMapper  extends Mapper<IndexName> {

    @Select({
            "<script>",
            "SELECT * FROM index_name",
            "WHERE `index` IN",
            "<foreach item='name' collection='names' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach>",
            "</script>"
    })

    List<IndexName> getShowName(@Param("names") List<String> names);

    @Select("select * from index_name where `index`=#{indexName} or show_name=#{showName}")
    List<IndexName> queryIndex(@Param("indexName") String indexName, @Param("showName") String showName);

    @Insert("INSERT INTO index_name (`index`, show_name) VALUES (#{indexName},#{showName}) ")
    void insertIndexName(@Param("indexName") String indexName, @Param("showName") String showName);
}
