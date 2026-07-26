package com.example.career_companion.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class Recruiter extends User{
    private String designation;
    private String department;
}
