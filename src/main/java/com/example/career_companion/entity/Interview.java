package com.example.career_companion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    private String interviewType;

    private LocalDateTime scheduledAt;

    private String location;

    private String feedback;

    private String status;

    @OneToMany(mappedBy = "interview")
    private List<InterviewRound> rounds = new ArrayList<>();
}
