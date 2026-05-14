package com.stu.demo3.service;

import com.stu.demo3.model.dto.ChatRequestDTO;
import com.stu.demo3.model.vo.ChatResponseVO;

public interface ChatService {
    ChatResponseVO chat(ChatRequestDTO requestDTO);
}