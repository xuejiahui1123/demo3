package com.stu.demo3.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRecord {
    private String sessionId;
    private String userMessage;
    private String assistantMessage;
    private LocalDateTime createTime;
}