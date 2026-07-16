package com.qa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String bio;
    private String email;

    @TableField("role")
    private Integer role;  // 0-普通用户 1-管理员

    @TableField("status")
    private Integer status; // 0-禁用 1-正常

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}