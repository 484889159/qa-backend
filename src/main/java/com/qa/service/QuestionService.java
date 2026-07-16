package com.qa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qa.entity.Question;
import com.qa.entity.vo.QuestionVO;

public interface QuestionService {
    boolean addQuestion(Question question);
    Page<QuestionVO> getQuestionList(Integer categoryId, Integer page, Integer size, String keyword);
    QuestionVO getQuestionDetail(Integer id);
    boolean updateQuestionStatus(Integer id, Integer status);
    boolean deleteQuestion(Integer id);
    boolean pinQuestion(Integer id, Integer isPinned);
}