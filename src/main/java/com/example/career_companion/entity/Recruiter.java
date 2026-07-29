package com.example.career_companion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Recruiter extends User {
    private String designation;
    private String department;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToMany(mappedBy = "recruiter")
    private List<Job> jobs = new ArrayList<>();
}
