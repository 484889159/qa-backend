package com.qa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reply")
public class Reply {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("question_id")
    private Integer questionId;

    @TableField("user_id")
    private Integer userId;

    private String content;
    private String images;

    @TableField("like_count")
    private Integer likeCount;

    private Integer status; // 0-待审核 1-已通过 2-已拒绝

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}