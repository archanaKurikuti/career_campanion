package com.example.career_companion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String requirements;

    private String responsibilities;

    private String location;

    private String jobType;

    private String experienceLevel;

    private Double salaryMin;

    private Double salaryMax;

    private String workplaceType;

    private Boolean isActive;

    private LocalDateTime postedAt;

    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recruiter recruiter;

    @OneToMany(mappedBy = "job")
    private List<Application> applications = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if (postedAt == null) {
            postedAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}
