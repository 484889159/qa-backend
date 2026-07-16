package com.qa.controller;

import com.qa.common.Result;
import com.qa.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIController {

    @Autowired
    private AIService aiService;

    /**
     * 智能问答接口
     */
    @PostMapping("/ask")
    public Result ask(@RequestBody Map<String, String> params) {
        String question = params.get("question");

        if (question == null || question.trim().isEmpty()) {
            return Result.error("请输入您的问题");
        }

        String answer = aiService.getAnswer(question);

        Map<String, Object> result = new HashMap<>();
        result.put("question", question);
        result.put("answer", answer);
        result.put("timestamp", System.currentTimeMillis());

        return Result.success(result);
    }

    /**
     * 获取热门问题
     */
    @GetMapping("/hot")
    public Result getHotQuestions() {
        return Result.success(aiService.getHotQuestions());
    }
}