package com.example.career_companion.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileUrl;

    private String content;

    private String summary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
