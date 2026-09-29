package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String designation;
    private String department;
    private Long companyId;
    private String companyName;
}
