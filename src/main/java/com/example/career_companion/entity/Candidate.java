package com.example.career_companion.entity;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class Candidate extends User{
    
    private String education;
    private Integer experience;
    private String location;
    @Column(length=1000)
    private String bio;
    private String github;
    private String linkedin;
    private String portfolio;
}
