package com.example.career_companion.repository;

import com.example.career_companion.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}