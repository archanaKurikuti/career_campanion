# Career Companion — AI-Powered Career & Recruitment Platform

**Career Companion** is a full-stack recruitment and career platform built with **Java 21**, **Spring Boot**, **MySQL**, **Spring Security (JWT)**, **Google Gemini AI**, and **React (Vite)**.

The platform seamlessly connects **Candidates ↔ Recruiters ↔ Companies ↔ Jobs** while delivering AI-powered career insights such as **Resume Analysis**, **Job Matching**, and **Career Advice**.

---

## 1. Features Overview

### Candidate Features
* **Authentication**: Register, login, and JWT-based session management.
* **Profile Management**: Update skills, experience, education, bio, GitHub, LinkedIn, portfolio links, and salary expectations.
* **Resume Management**: Upload PDF resumes and extract text for AI evaluation.
* **Job Search & Filtering**: Multi-criteria search (keyword, location, salary, experience, job type, employment type) with JPA pagination.
* **Job Applications**: Submit applications with cover letters and track status updates in real-time.
* **Notifications**: Receive notifications when application status changes or new jobs/recommendations occur.
* **Gemini AI Features**:
  * **Resume Analysis**: Intelligent breakdown of skills, strengths, missing skills, quality score, and actionable improvements.
  * **Job Match Analysis**: Score candidate compatibility against job postings with detailed explanations.
  * **Career Advice**: Tailored career growth recommendations based on skills and goals.

### Recruiter Features
* **Company Profile Management**: Create and manage company entities (with `verified` status controlled securely by backend logic).
* **Job Postings Management**: Create, update, and delete jobs linked strictly to the recruiter's company.
* **Applicant Inspection**: View candidate profiles, applications, and attached resumes.
* **Application Workflow**: Update application statuses (`APPLIED`, `UNDER_REVIEW`, `SHORTLISTED`, `INTERVIEW_SCHEDULED`, `SELECTED`, `REJECTED`) and attach recruiter remarks.

---

## 2. Architecture & Tech Stack

```text
React Frontend (Vite, Axios, React Router)
       ↓
REST Controllers (Spring Web MVC, OpenAPI/Swagger)
       ↓
Service Layer (Spring Services & AIService)
       ↓
Repository Layer (Spring Data JPA)
       ↓
MySQL Database (Hibernate ORM)
```

```text
Controller Layer
       ↓
   AIService
       ↓
 GeminiService
       ↓
Google Gemini API
```

### Technology Stack
* **Backend**: Java 21, Spring Boot 3.3.5 / 4.x, Spring Data JPA, Hibernate, MySQL, Spring Security, JWT (jjwt 0.12.6), BCrypt, Bean Validation, Lombok, OpenAPI/Swagger, JUnit 5, Mockito.
* **AI Integration**: Google Gemini API (`gemini-1.5-flash`).
* **Frontend**: React, Vite, JavaScript, Axios, React Router, Custom CSS Design System.
* **DevOps**: Docker, Docker Compose, Maven.

---

## 3. Database Design & Entity Model

```text
User (Inheritance: JOINED)
 ├── Candidate 1 ---- 1 Resume
 │        1 ---------- * Application * ---------- 1 Job
 │        * ---------- * Skill
 └── Recruiter 1 ---- * Company 1 ---- * Job
```

* **User**: Common authentication entity (`id`, `name`, `email`, `password`, `phone`, `role`, `createdAt`, `updatedAt`).
* **Candidate**: Inherits `User`. Adds `education`, `experience`, `currentCompany`, `location`, `bio`, `github`, `linkedin`, `portfolio`, `expectedSalary`, `openToWork`.
* **Recruiter**: Inherits `User`. Adds `designation`, `department`, relationship to `Company` and `Job`.
* **Company**: Stores company metadata with business rule `verified = false` on creation.
* **Job**: Job specifications (`title`, `description`, `requirements`, `location`, `salary`, `employmentType`, `jobType`, `experienceRequired`, `deadline`).
* **Application**: Job application tracking with AI match score, recruiter remarks, and status enum.
* **Resume**: File metadata and extracted text content for AI integration.
* **Notification**: System & application event alerts for users.

---

## 4. Security & Authentication Flow

1. **Registration / Login**: Passwords hashed with **BCrypt**.
2. **JWT Generation**: Authenticated users receive a signed JWT token containing email and roles.
3. **Stateless Authorization**: `JwtAuthenticationFilter` validates tokens per request and sets Spring `SecurityContext`.
4. **Role & Ownership Protection**:
   * Public: `POST /api/auth/**`, `GET /api/jobs/**`, `GET /api/companies/**`, `/swagger-ui/**`.
   * Candidate/Recruiter: `GET/PUT /api/candidates/me`, `GET/PUT /api/recruiters/me`.
   * Recruiter Only: `POST/PUT/DELETE /api/jobs/**`.
   * Resource Ownership: Recruiters can only modify jobs they own.

---

## 5. Gemini AI Architecture & Endpoints

AI functionality is abstracted behind `AIService` and `GeminiService` to ensure clean separation of concerns and graceful fallback if an API key is omitted:

1. **Resume Analysis**: `POST /api/ai/resume-analysis`
   * *Body*: `{ "resumeId": 1 }` or `POST /api/ai/analyze-resume/{candidateId}`
   * Returns: `detectedSkills`, `strengths`, `missingSkills`, `resumeQualityScore`, `suggestedImprovements`, `recommendedRoles`.
2. **Job Match**: `POST /api/ai/job-match`
   * *Body*: `{ "jobId": 10, "candidateId": 1 }` or `GET /api/ai/match?candidateId=1&jobId=10`
   * Returns: `matchScore`, `matchedSkills`, `missingSkills`, `explanation`.
3. **Career Advice**: `POST /api/ai/career-advice`
   * *Body*: `{ "question": "How do I transition to Senior Java Backend Engineer?", "candidateId": 1 }`
   * Returns: `{ "advice": "..." }`

---

## 6. API Quick Reference (Example Requests)

### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password123!",
  "role": "CANDIDATE"
}
```

### Search Jobs (Paginated)
```http
GET /api/jobs?keyword=Java&location=Remote&page=0&size=10&sortDir=desc
```

### AI Job Match
```http
POST /api/ai/job-match
Content-Type: application/json

{
  "candidateId": 1,
  "jobId": 5
}
```

---

## 7. How to Run locally

### Backend (Spring Boot)
```bash
# Clean and run unit test suite
mvn clean test

# Run application locally
mvn spring-boot:run
```
Swagger UI will be accessible at: `http://localhost:8080/swagger-ui.html`

### Frontend (React Vite)
```bash
cd frontend
npm install
npm run dev
```
Frontend will be accessible at: `http://localhost:5173`

---

## 8. Docker Setup & Deployment

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```
2. Start containers via Docker Compose:
   ```bash
   docker-compose up --build
   ```
This launches:
* **MySQL Database** on port `3306`
* **Spring Boot Backend** on port `8080`
* **React Frontend** on port `3000`

---

## 9. Testing & Quality Assurance

Run the automated test suite using Maven:
```bash
mvn clean test
```
* **JUnit 5** & **Mockito** cover `JobService`, `AuthService`, `AIService`, and application security configurations.

---

## 10. Future Enhancements
* Real-time WebSocket notifications.
* Advanced resume PDF parsing with Apache Tika.
* Recruiter candidate bookmarking and interview scheduling.
