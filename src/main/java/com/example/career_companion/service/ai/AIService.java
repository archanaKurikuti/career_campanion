package com.example.career_companion.service.ai;

import com.example.career_companion.dto.AIJobMatchResponse;
import com.example.career_companion.dto.AIResumeAnalysisResponse;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.Resume;

import java.util.List;

public interface AIService {

    AIResumeAnalysisResponse analyzeResume(String resumeContent, Candidate candidate);

    AIJobMatchResponse matchJob(Candidate candidate, Resume resume, Job job);

    List<AIJobMatchResponse> recommendJobs(Candidate candidate, Resume resume, List<Job> availableJobs);

    String getCareerAdvice(String question, Candidate candidate, Resume resume);
}
