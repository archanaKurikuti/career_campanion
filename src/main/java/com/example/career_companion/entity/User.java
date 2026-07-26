package com.example.career_companion.entity;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.JOINED)
    private Long id;
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @Column(unique = true,nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)    
    private Role role;
    private Boolean enabled=true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
