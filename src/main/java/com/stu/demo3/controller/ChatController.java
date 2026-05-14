package com.stu.demo3.controller;

import com.stu.demo3.common.Result;
import com.stu.demo3.model.dto.ChatRequestDTO;
import com.stu.demo3.model.vo.ChatResponseVO;
import com.stu.demo3.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Result<ChatResponseVO> chat(@RequestBody ChatRequestDTO requestDTO) {
        ChatResponseVO vo = chatService.chat(requestDTO);
        return Result.success(vo);
    }
}