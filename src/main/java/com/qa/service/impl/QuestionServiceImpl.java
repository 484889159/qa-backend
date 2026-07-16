package com.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qa.entity.Question;
import com.qa.entity.User;
import com.qa.entity.Category;
import com.qa.entity.vo.QuestionVO;
import com.qa.mapper.QuestionMapper;
import com.qa.mapper.UserMapper;
import com.qa.mapper.CategoryMapper;
import com.qa.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public boolean addQuestion(Question question) {
        // 直接设置为已通过，跳过审核
        question.setStatus(1);
        question.setViewCount(0);
        question.setReplyCount(0);
        question.setLikeCount(0);
        question.setIsPinned(0);
        return questionMapper.insert(question) > 0;
    }

    @Override
    public Page<QuestionVO> getQuestionList(Integer categoryId, Integer page, Integer size, String keyword) {
        Page<Question> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, 1); // 只查询已通过的

        if (categoryId != null && categoryId > 0) {
            wrapper.eq(Question::getCategoryId, categoryId);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(Question::getTitle, keyword)
                    .or()
                    .like(Question::getContent, keyword)
            );
        }

        wrapper.orderByDesc(Question::getIsPinned)
                .orderByDesc(Question::getCreateTime);

        Page<Question> questionPage = questionMapper.selectPage(pageParam, wrapper);

        // 转换为 VO
        Page<QuestionVO> voPage = new Page<>();
        BeanUtils.copyProperties(questionPage, voPage);

        voPage.setRecords(questionPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(java.util.stream.Collectors.toList()));

        return voPage;
    }

    @Override
    public QuestionVO getQuestionDetail(Integer id) {
        // 增加浏览量
        questionMapper.incrementViewCount(id);

        Question question = questionMapper.selectById(id);
        if (question == null) {
            return null;
        }
        return convertToVO(question);
    }

    @Override
    public boolean updateQuestionStatus(Integer id, Integer status) {
        Question question = new Question();
        question.setId(id);
        question.setStatus(status);
        return questionMapper.updateById(question) > 0;
    }

    @Override
    public boolean deleteQuestion(Integer id) {
        return questionMapper.deleteById(id) > 0;
    }

    @Override
    public boolean pinQuestion(Integer id, Integer isPinned) {
        Question question = new Question();
        question.setId(id);
        question.setIsPinned(isPinned);
        return questionMapper.updateById(question) > 0;
    }

    private QuestionVO convertToVO(Question question) {
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);

        // 获取用户信息
        User user = userMapper.selectById(question.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }

        // 获取分类信息
        Category category = categoryMapper.selectById(question.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        return vo;
    }
}