package com.example.career_companion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Candidate extends User {

    private String education;

    private Integer experience;

    private String currentCompany;

    private String location;

    private String bio;

    private String github;

    private String linkedin;

    private String portfolio;

    private String expectedSalary;

    private Boolean openToWork;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    private Resume resume;

    @OneToMany(mappedBy = "candidate")
    private List<Application> applications = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();
}