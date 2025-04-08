package com.example.puzzle.manager;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager manager;

    @Test
    void doChat() {
        String systemPrompt = "你是一位程序员";
        String userPrompt = "帮我写一个go语言的hello world";
        String s = manager.doChat(systemPrompt, userPrompt);
        System.out.println(s);
    }
}