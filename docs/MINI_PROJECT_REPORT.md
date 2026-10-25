# Academic Mini-Project Report: Student Placement Management System (SPMS)

**Project Title:** Student Placement Management System  
**Academic Domain:** Web Application Development / Software Engineering  
**Technology Stack:** Java 17, Spring Boot 3.3.4, Spring Security 6, Spring Data JPA, Hibernate ORM, MySQL 8.0, Thymeleaf, HTML5/CSS3/JavaScript, Bootstrap 5, JUnit 5  

---

## 1. Abstract
The **Student Placement Management System (SPMS)** is an enterprise-grade, server-rendered web application engineered to automate, streamline, and govern campus recruitment operations in higher educational institutions. Traditional campus placement drives often suffer from scattered records, manual CGPA eligibility verification across spreadsheets, unmonitored duplicate submissions, and delays in communicating interview schedules. 

The developed solution introduces a centralized, role-based portal catering to two primary stakeholders: **Students** and **Placement Officers**. Built strictly using Java 17, Spring Boot 3, and a layered Model-View-Controller (MVC) architecture, the system enforces server-side eligibility checks (CGPA thresholds, allowed academic branches, passing years, and deadline enforcement), guarantees single-application rules via relational database constraints, facilitates secure resume uploads, and delivers dynamic placement statistics with CSV export capabilities.

---

## 2. Introduction
Campus recruitment represents one of the most critical milestones in college education. Managing hundreds of graduating students and dozens of visiting corporate recruiters requires robust tracking of academic credentials, drive criteria, selection rounds, and offer letters.

The Student Placement Management System was architected to replace decentralized manual communication channels with an integrated portal offering real-time data synchronization between students and college placement officers.

---

## 3. Problem Statement
In conventional college placement workflows:
1. **Inefficient Verification:** Placement coordinators spend hundreds of hours manually filtering student spreadsheets against recruiter criteria (e.g., minimum 7.5 CGPA, no active backlogs, specific engineering branches).
2. **Data Inconsistency & Inaccuracies:** Students submit outdated resumes or multiple conflicting applications, creating confusion for recruiting companies.
3. **Lack of Transparency:** Students remain uninformed regarding their screening stage, test results, or reasons for ineligibility.
4. **Poor Auditability & Reporting:** Generating institutional placement reports for accreditation bodies (e.g., NAAC, NBA) requires arduous manual compilation.

---

## 4. Objectives of the Project
- **Automated Eligibility Engine:** Dynamically validate student eligibility in real-time based on CGPA, discipline, graduation year, and application deadlines.
- **Relational Integrity & Single Application Guarantee:** Prevent duplicate applications to the same drive using database-level unique constraints.
- **Role-Based Access Control (RBAC):** Restrict access through Spring Security, ensuring students cannot access administrative features and public registration is limited to student accounts.
- **Secure Document Management:** Support resume uploads (.pdf, .doc, .docx up to 5MB) with strict MIME-type and size validation.
- **Actionable Administrative Analytics:** Provide real-time graphical charts and automated CSV report generation for placement drives and branch statistics.

---

## 5. Existing System vs. Proposed System

| Dimension | Existing Manual / Spreadsheet System | Proposed Student Placement Management System |
|---|---|---|
| **Eligibility Verification** | Manual filtering on Google Sheets/Excel (prone to errors). | Automated, instantaneous server-side validation against job rules. |
| **Duplicate Applications** | Hard to detect across multiple emails or forms. | Enforced by composite database unique constraint `(student_id, job_id)`. |
| **Security & Authentication** | Plaintext records, shared spreadsheets, no RBAC. | BCrypt password hashing, Spring Security 6, session guards, CSRF protection. |
| **Student Experience** | Blind applications with no feedback on why rejected. | Transparent feedback specifying exact criteria causing ineligibility. |
| **Placement Analytics** | Manual calculations taking weeks before accreditation. | Real-time interactive charts (Chart.js) and instant CSV export. |
| **Resume Handling** | Email attachments prone to size limits and disorganization. | Centralized database/cloud storage with controlled download access. |

---

## 6. Software & Hardware Requirements

### Software Requirements
- **Operating System:** Linux (Ubuntu/Debian), macOS, or Windows 10/11
- **JDK:** Java Development Kit 17 LTS or higher
- **Build Tool:** Apache Maven 3.8+
- **Framework:** Spring Boot 3.3.4
- **ORM / Persistence:** Spring Data JPA / Hibernate 6.x / HikariCP
- **Database Engine:** MySQL 8.0+ (Production) / H2 in-memory (Dev/Test Fallback)
- **Templating Engine:** Thymeleaf 3.1+ with Spring Security 6 dialect
- **UI Framework:** Bootstrap 5.3, Bootstrap Icons, Chart.js 4.4

### Hardware Requirements
- **Processor:** Dual-Core 2.0 GHz or higher (x86_64 / ARM64)
- **RAM:** Minimum 4 GB (8 GB recommended for concurrent builds)
- **Disk Space:** 500 MB free space for JDK, dependencies, and application data

---

## 7. System Design & Layered Architecture

The system strictly adheres to the industry-standard **Layered MVC Pattern**:

1. **Presentation Layer (Thymeleaf Templates + Bootstrap 5):**
   - Server-side rendered HTML5 views enriched with Thymeleaf expressions (`th:*`, `sec:*`).
   - Client interactivity using Bootstrap 5 modals, alerts, and Chart.js.
2. **Security & Controller Layer (Spring MVC):**
   - Intercepts HTTP requests, validates CSRF tokens, extracts user principals from Spring Security context.
   - Maps requests via `@Controller`, delegates to services, binds form inputs with Jakarta Bean Validation (`@Valid`).
3. **Service Layer (Business Logic):**
   - Transactional business logic annotated with `@Transactional`.
   - Core eligibility engine evaluates student profile vs. job posting criteria.
   - Guard rules enforce allowed application state transitions (e.g., APPLIED -> SHORTLISTED -> SELECTED).
4. **Data Access Layer (Spring Data JPA / Repositories):**
   - Extends `JpaRepository<T, ID>` with custom JPQL queries and derived queries.
   - Hibernate translates entity operations to parameterized SQL statements.
5. **Database Layer (MySQL / JDBC):**
   - Relational database storing normalized records with constraints and foreign keys.

---

## 8. Database Design & Normalization
The database schema was normalized to Third Normal Form (3NF) to eliminate insertion, update, and deletion anomalies.
- `users`: Stores login identity, hashed password, role (`STUDENT`, `PLACEMENT_OFFICER`), and active flag.
- `student_profiles`: One-to-one relationship with `users`. Stores full name, roll number, department, branch, CGPA, 10th/12th percentages, skills, and binary resume data.
- `companies`: Stores verified recruiters, sector, address, and HR contact points.
- `job_postings`: Many-to-one with `companies`. Contains package (LPA), minimum CGPA cutoff, eligible branches, vacancies, deadline, and selection process.
- `job_applications`: Many-to-one with both `student_profiles` and `job_postings`. Enforces unique constraint on `(student_id, job_posting_id)`.
- `announcements`: Broadcast messages with priority rankings (`NORMAL`, `HIGH`, `URGENT`).
- `interview_schedules`: Details specific test/interview rounds, venues, and timings.

---

## 9. Implementation Highlights

### 9.1 Multi-Criteria Eligibility Engine
The core method `JobPostingService.checkEligibility(StudentProfile student, JobPosting job)` executes six distinct validation checks:
1. **Drive Active State:** Confirms `job.active == true` and `job.status == JobStatus.OPEN`.
2. **Application Deadline:** Compares `job.applicationDeadline` against `LocalDateTime.now()`.
3. **Academic Cutoff (CGPA):** Ensures `student.cgpa >= job.minimumCgpa`.
4. **Discipline / Branch Match:** Checks if `job.eligibleBranches` equals `"ALL"` or contains the student's branch code.
5. **Graduation Batch:** Validates expected completion year if targeted by the recruiter.
6. **Resume Compliance:** Verifies that a valid resume has been uploaded by the student.

### 9.2 Duplicate Submission Prevention
Handled at two levels:
1. **Service Level Check:** Queries `applicationRepository.existsByStudentIdAndJobPostingId(studentId, jobId)` and throws `DuplicateApplicationException`.
2. **Database Level Defense:** Schema table `job_applications` has `CONSTRAINT uk_student_job_application UNIQUE (student_id, job_posting_id)`.

### 9.3 Safe Status Transitions
The application lifecycle follows a deterministic finite state machine:
- Initial state: `APPLIED`.
- Intermediate states: `SHORTLISTED` (with interview rounds recorded) or `REJECTED`.
- Terminal state: `SELECTED` (Placed).
- Direct reset from `SELECTED` back to `APPLIED` is blocked via `InvalidStatusTransitionException`.
- Student withdrawal is restricted strictly to the `APPLIED` phase.

---

## 10. Testing & Quality Assurance
The project includes automated JUnit 5 test suites covering:
1. **`EligibilityCheckTest`:** Unit tests verifying acceptance and rejection reasons across CGPA thresholds, branch constraints, expired deadlines, and missing resumes.
2. **`JobApplicationServiceTest`:** Mockito-based tests verifying duplicate application prevention, invalid status transitions, and student withdrawal rules.
3. **`RoleAuthorizationTest`:** Tests verifying that public registrations only create `Role.STUDENT` accounts with BCrypt-hashed passwords.

---

## 11. Results & Screenshots Description
- **Home Landing Page:** Displays college placement statistics (Placement Rate, Highest Package, Recruiter count), active notices ticker, workflow cards, and open drives.
- **Student Dashboard:** Features profile completion meter (0-100%), 4 status metric boxes, recent applications table with status badges and withdraw action, and resume downloader.
- **Job Catalog:** Multi-filter search (Keyword, Company, Branch, Min Package, Min CGPA) with green/red eligibility badges on each job card.
- **Officer Dashboard:** Real-time Chart.js graphs (Branch Placements, Status Funnel), recent applicants feed, and status update modals.
- **Reports Module:** Summary aggregations and one-click master CSV export.

---

## 12. Conclusion & Future Scope

### Conclusion
The Student Placement Management System successfully eliminates manual spreadsheet tracking and human error in campus recruitment. By combining Spring Boot 3, Spring Security, Hibernate ORM, and Thymeleaf, the system delivers high performance, strong data security, transparent student feedback, and auditable placement reporting.

### Future Scope
1. **AI Resume Screening:** Integration of automated semantic keyword matching against recruiter job descriptions.
2. **Automated Email/SMS Alerts:** Notification dispatched via SMTP whenever an application status changes or an interview is scheduled.
3. **Video Conferencing Integration:** WebRTC / Google Meet link generation for virtual interviews directly from the portal.
4. **Alumni Placement Mentorship:** Module allowing placed alumni to mentor final-year students.

---

## 13. References
1. Spring Boot Documentation: https://spring.io/projects/spring-boot
2. Spring Security Architecture: https://docs.spring.io/spring-security/reference/
3. Spring Data JPA Guide: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
4. Thymeleaf + Spring Tutorial: https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html
5. Hibernate ORM Reference Manual: https://hibernate.org/orm/documentation/
