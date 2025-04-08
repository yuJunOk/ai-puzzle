package com.example.puzzle.service.impl;

import com.example.puzzle.common.ResponseCode;
import com.example.puzzle.constant.AiConstant;
import com.example.puzzle.exception.BusinessException;
import com.example.puzzle.manager.AiManager;
import com.example.puzzle.pojo.vo.AiChatRoomVo;
import com.example.puzzle.service.AiChatService;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.puzzle.constant.AiConstant.END_CHAT_TAG;
import static com.example.puzzle.constant.AiConstant.START_CHAT_TAG;

/**
 * @author pengYuJun
 */
@Service
public class AiChatServiceImpl implements AiChatService {

    @Resource
    private AiManager aiManager;

    final Map<Long, List<ChatMessage>> globalMessages = new HashMap<>();

    @Override
    public String doChat(Long roomId, String message) {
        // 1. 准备消息列表、关联历史上下文
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(AiConstant.SYSTEM_PROMPT).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();

        List<ChatMessage> messages = new ArrayList<>();
        //
        if (!globalMessages.containsKey(roomId)) {
            if (START_CHAT_TAG.equals(message)){
                //首次开始时
                globalMessages.put(roomId, messages);
                messages.add(systemMessage);
            }else {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "请先开始游戏");
            }
        }else {
            messages = globalMessages.get(roomId);
        }
        messages.add(userMessage);
        // 2. 调用AI
        String answer = aiManager.doChat(messages);
        final ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(answer).build();
        messages.add(assistantMessage);
        if (answer.contains(END_CHAT_TAG)){
            globalMessages.remove(roomId);
        }
        // 3. 返回消息
        return answer;
    }

    @Override
    public List<AiChatRoomVo> getChatRoomList() {
        List<AiChatRoomVo> rooms = new ArrayList<>();
        for (Map.Entry<Long, List<ChatMessage>> roomIdMessageListEntry : globalMessages.entrySet()) {
            AiChatRoomVo room = new AiChatRoomVo(roomIdMessageListEntry.getKey(), roomIdMessageListEntry.getValue());
            rooms.add(room);
        }
        return rooms;
    }

    @Override
    public List<ChatMessage> getHistory(long roomId) {
        List<ChatMessage> chatMessages = Collections.emptyList();
        for (Map.Entry<Long, List<ChatMessage>> roomIdMessageListEntry : globalMessages.entrySet()) {
            if (roomIdMessageListEntry.getKey() == roomId) {
                chatMessages = roomIdMessageListEntry.getValue();
            }
        }
        return chatMessages;
    }
}
