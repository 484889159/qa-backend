package com.qa.controller;

import com.qa.common.Result;
import com.qa.entity.Reply;
import com.qa.entity.vo.ReplyVO;
import com.qa.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reply")
@CrossOrigin
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping("/add")
    public Result addReply(@RequestBody Reply reply) {
        if (reply.getContent() == null || reply.getContent().trim().isEmpty()) {
            return Result.error("回复内容不能为空");
        }
        if (reply.getQuestionId() == null) {
            return Result.error("问题ID不能为空");
        }

        boolean success = replyService.addReply(reply);
        return success ? Result.success("回复成功，等待审核") : Result.error("回复失败");
    }

    @GetMapping("/list/{questionId}")
    public Result getReplies(@PathVariable Integer questionId) {
        List<ReplyVO> replies = replyService.getRepliesByQuestionId(questionId);
        return Result.success(replies);
    }

    @PutMapping("/status")
    public Result updateStatus(@RequestBody Reply reply) {
        boolean success = replyService.updateReplyStatus(reply.getId(), reply.getStatus());
        return success ? Result.success("审核完成") : Result.error("操作失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteReply(@PathVariable Integer id) {
        boolean success = replyService.deleteReply(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}