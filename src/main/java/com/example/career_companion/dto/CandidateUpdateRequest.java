package com.example.career_companion.dto;

import jakarta.validation.constraints.Min;

public class CandidateUpdateRequest {

    private String education;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    private String currentCompany;

    private String location;

    private String bio;

    private String github;

    private String linkedin;

    private String portfolio;

    private String expectedSalary;

    private Boolean openToWork;

    private java.util.List<String> skills;

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getCurrentCompany() { return currentCompany; }
    public void setCurrentCompany(String currentCompany) { this.currentCompany = currentCompany; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGithub() { return github; }
    public void setGithub(String github) { this.github = github; }

    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }

    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }

    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }

    public Boolean getOpenToWork() { return openToWork; }
    public void setOpenToWork(Boolean openToWork) { this.openToWork = openToWork; }

    public java.util.List<String> getSkills() { return skills; }
    public void setSkills(java.util.List<String> skills) { this.skills = skills; }
}