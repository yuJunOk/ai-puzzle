package com.example.puzzle.controller;

import com.example.puzzle.common.ResponseEntity;
import com.example.puzzle.common.ResponseCode;
import com.example.puzzle.exception.BusinessException;
import com.example.puzzle.service.AiChatService;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pengYuJun
 */
@RestController
@RequestMapping("ai")
public class AiChatController {

    @Resource
    private AiChatService aiChatService;

    /**
     * 与AI对话
     * @param roomId
     * @param message
     * @return
     */
    @CrossOrigin
    @PostMapping("/{roomId}/send")
    public ResponseEntity<String> doChat(@PathVariable("roomId") long roomId, @RequestParam String message) {
        if (!StringUtils.hasText(message)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "发送消息不能为空");
        }
        String answer = aiChatService.doChat(roomId, message);
        return ResponseEntity.success(answer);
    }

    /**
     * 获取所有聊天室列表
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/{roomId}/history")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable("roomId") long roomId) {
        List<ChatMessage> messages = aiChatService.getHistory(roomId);
        return ResponseEntity.success(messages);
    }

}
