package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private String type;

    private Boolean isRead;

    private LocalDateTime createdAt;
}