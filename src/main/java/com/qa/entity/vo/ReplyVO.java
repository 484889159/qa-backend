package com.qa.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReplyVO {
    private Integer id;
    private Integer questionId;
    private Integer userId;
    private String nickname;
    private String avatar;
    private String content;
    private String images;
    private Integer likeCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}