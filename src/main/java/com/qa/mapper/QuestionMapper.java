package com.qa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qa.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    @Update("UPDATE question SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(@Param("id") Integer id);

    @Update("UPDATE question SET reply_count = reply_count + 1 WHERE id = #{id}")
    void incrementReplyCount(@Param("id") Integer id);
}