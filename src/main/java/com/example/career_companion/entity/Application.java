package com.example.career_companion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Job job;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime appliedAt;

    private String coverLetter;

    private Double aiMatchScore;

    private String recruiterRemarks;

    @OneToMany(mappedBy = "application")
    private List<Interview> interviews = new ArrayList<>();

    @OneToMany(mappedBy = "application")
    private List<Feedback> feedbacks = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ApplicationStatus.APPLIED;
        }
    }
}