package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterUpdateRequest {
    private String name;
    private String phone;
    private String designation;
    private String department;
    private Long companyId;
}
