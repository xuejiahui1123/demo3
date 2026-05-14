package com.stu.demo3.model.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String sessionId;
    private String message;
}