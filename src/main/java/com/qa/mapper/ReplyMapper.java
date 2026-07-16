package com.qa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qa.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ReplyMapper extends BaseMapper<Reply> {

    @Select("SELECT * FROM reply WHERE question_id = #{questionId} AND status = 1 ORDER BY create_time ASC")
    List<Reply> findByQuestionId(@Param("questionId") Integer questionId);
}