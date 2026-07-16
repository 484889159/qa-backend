package com.qa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("category_id")
    private Integer categoryId;

    private String title;
    private String content;
    private String images;

    @TableField("is_anonymous")
    private Integer isAnonymous; // 0-否 1-是

    @TableField("is_pinned")
    private Integer isPinned; // 0-否 1-是

    private Integer status; // 0-待审核 1-已通过 2-已拒绝

    @TableField("view_count")
    private Integer viewCount;

    @TableField("reply_count")
    private Integer replyCount;

    @TableField("like_count")
    private Integer likeCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}