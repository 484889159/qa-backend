package com.qa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qa.common.Result;
import com.qa.entity.Question;
import com.qa.entity.vo.QuestionVO;
import com.qa.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    public Result addQuestion(@RequestBody Question question) {
        // 验证
        if (question.getTitle() == null || question.getTitle().trim().isEmpty()) {
            return Result.error("标题不能为空");
        }
        if (question.getContent() == null || question.getContent().trim().isEmpty()) {
            return Result.error("内容不能为空");
        }
        if (question.getCategoryId() == null) {
            return Result.error("请选择分类");
        }

        boolean success = questionService.addQuestion(question);
        return success ? Result.success("发布成功，等待审核") : Result.error("发布失败");
    }

    @GetMapping("/list")
    public Result getQuestionList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {

        Page<QuestionVO> questionPage = questionService.getQuestionList(categoryId, page, size, keyword);
        return Result.success(questionPage);
    }

    @GetMapping("/detail/{id}")
    public Result getQuestionDetail(@PathVariable Integer id) {
        QuestionVO question = questionService.getQuestionDetail(id);
        if (question == null) {
            return Result.error("问题不存在");
        }
        return Result.success(question);
    }

    @PutMapping("/status")
    public Result updateStatus(@RequestBody Question question) {
        // 管理员审核
        boolean success = questionService.updateQuestionStatus(question.getId(), question.getStatus());
        return success ? Result.success("审核完成") : Result.error("操作失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteQuestion(@PathVariable Integer id) {
        boolean success = questionService.deleteQuestion(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    @PutMapping("/pin")
    public Result pinQuestion(@RequestBody Question question) {
        boolean success = questionService.pinQuestion(question.getId(), question.getIsPinned());
        return success ? Result.success("操作成功") : Result.error("操作失败");
    }
}