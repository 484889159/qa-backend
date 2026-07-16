package com.qa.service;

import com.qa.entity.Reply;
import com.qa.entity.vo.ReplyVO;
import java.util.List;

public interface ReplyService {
    boolean addReply(Reply reply);
    List<ReplyVO> getRepliesByQuestionId(Integer questionId);
    boolean updateReplyStatus(Integer id, Integer status);
    boolean deleteReply(Integer id);
}