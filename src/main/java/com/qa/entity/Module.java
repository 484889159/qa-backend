package com.qa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("module")
public class Module {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String icon;
    private String name;
    private String desc;
    private String path;
    private String image;
    private Integer views;
    private Integer likes;
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}