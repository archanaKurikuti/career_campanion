package com.example.career_companion.repository;

import com.example.career_companion.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

	java.util.List<Application> findByCandidateId(Long candidateId);

	java.util.List<Application> findByJobId(Long jobId);
}