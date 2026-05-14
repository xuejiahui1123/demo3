package com.stu.demo3.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.stu.demo3.model.dto.ChatRequestDTO;
import com.stu.demo3.model.vo.ChatResponseVO;
import com.stu.demo3.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final StringRedisTemplate redisTemplate;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder,
                           StringRedisTemplate redisTemplate) {
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一名专业、友好、简洁的中文智能助手，请结合历史对话回答用户问题。")
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ChatResponseVO chat(ChatRequestDTO requestDTO) {
        String sessionId = requestDTO.getSessionId();
        String message = requestDTO.getMessage();
        String redisKey = "chat:session:" + sessionId;

        // 1. 读取历史对话
        List<String> historyList = redisTemplate.opsForList().range(redisKey, 0, -1);
        String historyText = "";
        if (historyList != null && !historyList.isEmpty()) {
            historyText = String.join("\n", historyList);
        }

        // 2. 拼接上下文
        String prompt = """
                以下是历史对话：
                %s
                当前用户问题：
                %s
                """.formatted(historyText, message);

        // 3. 调用大模型
        String answer = chatClient.prompt(prompt).call().content();

        // 4. 保存本轮记录
        String record = "用户：" + message + "\n助手：" + answer;
        redisTemplate.opsForList().rightPush(redisKey, record);

        // 5. 只保留最近 3 轮
        Long size = redisTemplate.opsForList().size(redisKey);
        if (size != null && size > 3) {
            redisTemplate.opsForList().trim(redisKey, size - 3, size - 1);
        }

        return new ChatResponseVO(message, answer);
    }
}