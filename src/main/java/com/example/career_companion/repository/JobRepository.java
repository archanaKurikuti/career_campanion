package com.example.career_companion.repository;

import com.example.career_companion.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByCompanyId(Long companyId);
    List<Job> findByRecruiterId(Long recruiterId);
    List<Job> findByIsActiveTrue();
}
