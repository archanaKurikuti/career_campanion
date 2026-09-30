package com.example.career_companion.service.ai;

import com.example.career_companion.dto.AICareerAdviceRequest;
import com.example.career_companion.dto.AIJobMatchResponse;
import com.example.career_companion.dto.AIResumeAnalysisResponse;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.Resume;
import com.example.career_companion.entity.Skill;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.JobRepository;
import com.example.career_companion.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

    private final GeminiService geminiService;
    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;

    public AIServiceImpl(GeminiService geminiService) {
        this(geminiService, null, null, null);
    }

    @Autowired
    public AIServiceImpl(
            GeminiService geminiService,
            CandidateRepository candidateRepository,
            ResumeRepository resumeRepository,
            JobRepository jobRepository
    ) {
        this.geminiService = geminiService;
        this.candidateRepository = candidateRepository;
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
    }


  /*  @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.model:gpt-3.5-turbo}")
    private String model;*/

    private static final Set<String> POPULAR_TECH_SKILLS = Set.of(
            "Java", "Spring Boot", "MySQL", "REST API", "React", "JavaScript", "TypeScript",
            "Python", "Docker", "AWS", "Kubernetes", "Microservices", "Git", "Node.js",
            "HTML", "CSS", "SQL", "PostgreSQL", "MongoDB", "Spring Security", "Hibernate"
    );

    @Override
    public AIResumeAnalysisResponse analyzeResume(String resumeContent, Candidate candidate) {
        logger.info("Analyzing resume for candidate ID: {}", candidate != null ? candidate.getId() : "Unknown");

        String contentToAnalyze = (resumeContent != null ? resumeContent : "") + " "
                + (candidate != null && candidate.getBio() != null ? candidate.getBio() : "") + " "
                + (candidate != null && candidate.getEducation() != null ? candidate.getEducation() : "");

        List<String> candidateSkills = candidate != null && candidate.getSkills() != null
                ? candidate.getSkills().stream().map(Skill::getName).collect(Collectors.toList())
                : new ArrayList<>();

        List<String> detectedSkills = new ArrayList<>(candidateSkills);
        for (String skill : POPULAR_TECH_SKILLS) {
            if (!detectedSkills.stream().anyMatch(s -> s.equalsIgnoreCase(skill))
                    && contentToAnalyze.toLowerCase().contains(skill.toLowerCase())) {
                detectedSkills.add(skill);
            }
        }

        List<String> missingSkills = POPULAR_TECH_SKILLS.stream()
                .filter(s -> !detectedSkills.stream().anyMatch(ds -> ds.equalsIgnoreCase(s)))
                .limit(4)
                .collect(Collectors.toList());

        List<String> strengths = new ArrayList<>();
        if (!detectedSkills.isEmpty()) {
            strengths.add("Strong foundation in " + String.join(", ", detectedSkills.stream().limit(3).toList()));
        }
        if (candidate != null && candidate.getExperience() != null && candidate.getExperience() > 0) {
            strengths.add("Proven hands-on experience (" + candidate.getExperience() + " years)");
        } else {
            strengths.add("Active job seeker with structured skill background");
        }
        if (candidate != null && candidate.getEducation() != null) {
            strengths.add("Relevant educational background: " + candidate.getEducation());
        }

        List<String> weaknesses = new ArrayList<>();
        if (missingSkills.size() > 0) {
            weaknesses.add("Lacks formal experience with modern cloud/container tools like " + String.join(", ", missingSkills.stream().limit(2).toList()));
        }
        if (detectedSkills.size() < 4) {
            weaknesses.add("Limited listed technical skills on profile");
        }

        List<String> suggestedImprovements = List.of(
                "Add quantifiable achievements to project descriptions.",
                "Include certifications or hands-on projects for " + (missingSkills.isEmpty() ? "Docker/AWS" : missingSkills.get(0)) + ".",
                "Keep contact links (GitHub, LinkedIn) updated."
        );

        List<String> recommendedRoles = List.of(
                "Full Stack Developer",
                "Java Backend Engineer",
                "Software Development Engineer (SDE-1)",
                "Web Application Developer"
        );

        int qualityScore = Math.min(100, Math.max(50, 60 + detectedSkills.size() * 5 + (candidate != null && candidate.getExperience() != null ? candidate.getExperience() * 3 : 0)));

        return AIResumeAnalysisResponse.builder()
                .detectedSkills(detectedSkills)
                .experienceSummary(candidate != null && candidate.getExperience() != null ? candidate.getExperience() + " years of relevant experience" : "Entry / Intermediate Level")
                .educationSummary(candidate != null && candidate.getEducation() != null ? candidate.getEducation() : "Higher Education Degree")
                .strengths(strengths)
                .weaknesses(weaknesses)
                .missingSkills(missingSkills)
                .resumeQualityScore(qualityScore)
                .suggestedImprovements(suggestedImprovements)
                .recommendedRoles(recommendedRoles)
                .build();
    }

    @Override
    public AIJobMatchResponse matchJob(Candidate candidate, Resume resume, Job job) {
        if (job == null) {
            return AIJobMatchResponse.builder()
                    .matchScore(0.0)
                    .explanation("Job description is missing")
                    .build();
        }

        List<String> candidateSkills = candidate != null && candidate.getSkills() != null
                ? candidate.getSkills().stream().map(Skill::getName).map(String::toLowerCase).toList()
                : new ArrayList<>();

        String jobText = (job.getTitle() + " " + job.getDescription() + " "
                + (job.getRequirements() != null ? job.getRequirements() : "")).toLowerCase();

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String tech : POPULAR_TECH_SKILLS) {
            boolean requiredInJob = jobText.contains(tech.toLowerCase());
            boolean candidateHas = candidateSkills.contains(tech.toLowerCase());

            if (requiredInJob) {
                if (candidateHas) {
                    matchedSkills.add(tech);
                } else {
                    missingSkills.add(tech);
                }
            }
        }

        double score = 60.0;
        if (!matchedSkills.isEmpty()) score += Math.min(30.0, matchedSkills.size() * 10.0);
        if (candidate != null && candidate.getExperience() != null && job.getExperienceRequired() != null) {
            if (candidate.getExperience() >= job.getExperienceRequired()) score += 10.0;
            else score -= 5.0;
        }

        score = Math.min(98.0, Math.max(45.0, score));

        String explanation = "Matched " + matchedSkills.size() + " key skills required for " + job.getTitle()
                + ". Missing " + missingSkills.size() + " optional/required skills.";

        return AIJobMatchResponse.builder()
                .jobId(job.getId())
                .jobTitle(job.getTitle())
                .companyName(job.getCompany() != null ? job.getCompany().getCompanyName() : "Company")
                .matchScore(Math.round(score * 10.0) / 10.0)
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .explanation(explanation)
                .build();
    }

    @Override
    public List<AIJobMatchResponse> recommendJobs(Candidate candidate, Resume resume, List<Job> availableJobs) {
        if (availableJobs == null || availableJobs.isEmpty()) {
            return Collections.emptyList();
        }

        return availableJobs.stream()
                .map(job -> matchJob(candidate, resume, job))
                .sorted(Comparator.comparingDouble(AIJobMatchResponse::getMatchScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public String getCareerAdvice(String question, Candidate candidate, Resume resume) {

        String name = candidate != null && candidate.getName() != null
                ? candidate.getName()
                : "Job Seeker";

        String candidateSkills = candidate != null && candidate.getSkills() != null
                ? candidate.getSkills()
                        .stream()
                        .map(Skill::getName)
                        .collect(Collectors.joining(", "))
                : "No skills provided";

        String education = candidate != null && candidate.getEducation() != null
                ? candidate.getEducation()
                : "Not provided";

        String experience = candidate != null && candidate.getExperience() != null
                ? candidate.getExperience() + " years"
                : "Not provided";

        if (question == null || question.isBlank()) {
            question = "How can I improve my career prospects?";
        }

        String prompt = """
                You are a career guidance assistant.

                Candidate name: %s
                Skills: %s
                Education: %s
                Experience: %s

                Candidate question:
                %s

                Give practical, personalized career advice.
                Consider the candidate's skills, education and experience.
                Give clear actionable steps.
                """.formatted(
                name,
                candidateSkills,
                education,
                experience,
                question
        );

        return geminiService.generateAdvice(prompt);
    }

    @Override
    public AIResumeAnalysisResponse analyzeResume(Long resumeId) {
        if (resumeRepository == null) {
            throw new IllegalStateException("ResumeRepository is not initialized");
        }
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + resumeId));
        Candidate candidate = resume.getCandidate();
        return analyzeResume(resume.getContent(), candidate);
    }

    @Override
    public AIJobMatchResponse matchJob(Long candidateId, Long jobId) {
        if (candidateRepository == null || jobRepository == null) {
            throw new IllegalStateException("Repositories are not initialized");
        }
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        Resume resume = candidate.getResume();
        return matchJob(candidate, resume, job);
    }

    @Override
    public String getCareerAdvice(AICareerAdviceRequest request) {
        Candidate candidate = null;
        Resume resume = null;

        if (request != null && request.getCandidateId() != null && candidateRepository != null) {
            candidate = candidateRepository.findById(request.getCandidateId()).orElse(null);
            if (candidate != null) {
                resume = candidate.getResume();
            }
        }

        String question = request != null ? request.getQuestion() : "How can I improve my career prospects?";
        return getCareerAdvice(question, candidate, resume);
    }
}

