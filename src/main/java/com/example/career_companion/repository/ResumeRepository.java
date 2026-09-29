package com.example.career_companion.repository;

import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByCandidateId(Long candidateId);
    Optional<Resume> findByCandidate(Candidate candidate);
}