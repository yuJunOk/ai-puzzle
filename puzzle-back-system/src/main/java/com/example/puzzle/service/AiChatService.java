package com.example.puzzle.service;

import com.example.puzzle.pojo.vo.AiChatRoomVo;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

import java.util.List;

/**
 * @author pengYuJun
 */
public interface AiChatService {

    /**
     * 和AI对话
     * @param roomId
     * @param message
     * @return
     */
    String doChat(Long roomId, String message);

    /**
     * 获取聊天室列表
     * @return
     */
    List<AiChatRoomVo> getChatRoomList();

    /**
     * 获取某房的历史列表
     * @param roomId
     * @return
     */
    List<ChatMessage> getHistory(long roomId);
}
