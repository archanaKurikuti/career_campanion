package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateResponse {

    private Long id;

    private String name;

    private String email;

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
}