package com.qa.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionVO {
    private Integer id;
    private Integer userId;
    private String nickname;
    private String avatar;
    private Integer categoryId;
    private String categoryName;
    private String title;
    private String content;
    private String images;
    private Integer isAnonymous;
    private Integer isPinned;
    private Integer status;
    private Integer viewCount;
    private Integer replyCount;
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}