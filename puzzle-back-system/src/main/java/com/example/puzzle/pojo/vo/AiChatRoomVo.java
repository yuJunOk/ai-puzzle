package com.example.puzzle.pojo.vo;

import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pengYuJun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiChatRoomVo {

    private Long roomId;

    private List<ChatMessage> chatMessageList;
}
