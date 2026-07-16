package com.qa.service.impl;

import com.qa.entity.Reply;
import com.qa.entity.User;
import com.qa.entity.vo.ReplyVO;
import com.qa.mapper.ReplyMapper;
import com.qa.mapper.QuestionMapper;
import com.qa.mapper.UserMapper;
import com.qa.service.ReplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public boolean addReply(Reply reply) {
        reply.setStatus(0); // 默认待审核
        reply.setLikeCount(0);

        int result = replyMapper.insert(reply);
        if (result > 0) {
            // 更新问题回复数
            questionMapper.incrementReplyCount(reply.getQuestionId());
        }
        return result > 0;
    }

    @Override
    public List<ReplyVO> getRepliesByQuestionId(Integer questionId) {
        List<Reply> replies = replyMapper.findByQuestionId(questionId);
        return replies.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateReplyStatus(Integer id, Integer status) {
        Reply reply = new Reply();
        reply.setId(id);
        reply.setStatus(status);
        return replyMapper.updateById(reply) > 0;
    }

    @Override
    public boolean deleteReply(Integer id) {
        return replyMapper.deleteById(id) > 0;
    }

    private ReplyVO convertToVO(Reply reply) {
        ReplyVO vo = new ReplyVO();
        BeanUtils.copyProperties(reply, vo);

        User user = userMapper.selectById(reply.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }

        return vo;
    }
}