# System Architecture & Diagrams

This document contains visual diagrams for the **Student Placement Management System**, including Use-Case Diagrams, Data Flow Diagrams (DFD Level 0 and Level 1), and the Layered MVC Architecture.

---

## 1. Use-Case Diagram

```mermaid
graph TD
    subgraph Actors
        Student["fa:fa-user Student"]
        Officer["fa:fa-user-tie Placement Officer"]
    end

    subgraph "Student Placement Management System"
        UC1["Register & Log In"]
        UC2["Maintain Profile & Upload Resume"]
        UC3["Browse Open Job Drives"]
        UC4["Server-side Eligibility Verification"]
        UC5["Submit Job Application"]
        UC6["Track Application Status & Withdraw"]
        UC7["View Placement Notices & Schedules"]

        UC8["Manage Partner Companies (CRUD)"]
        UC9["Create & Publish Job Openings"]
        UC10["Review Applicants by Drive & Criteria"]
        UC11["Shortlist / Select / Reject Candidates"]
        UC12["Manage Student Registry & Deactivate"]
        UC13["Broadcast Announcements"]
        UC14["Generate Placement Analytics & CSV Reports"]
    end

    Student --> UC1
    Student --> UC2
    Student --> UC3
    Student --> UC4
    Student --> UC5
    Student --> UC6
    Student --> UC7

    Officer --> UC1
    Officer --> UC8
    Officer --> UC9
    Officer --> UC10
    Officer --> UC11
    Officer --> UC12
    Officer --> UC13
    Officer --> UC14

    UC5 -.->|includes| UC4
```

---

## 2. Data Flow Diagram (DFD)

### Level 0: Context Level DFD
```mermaid
flowchart TD
    S["Student"]
    SPMS["Student Placement Management System (SPMS)"]
    PO["Placement Officer"]
    DB[("MySQL Database")]

    S -->|"Credentials, Profile, Resume, Applications"| SPMS
    SPMS -->|"Eligibility Status, Job Drives, Application Feedback"| S

    PO -->|"Job Openings, Company Info, Announcements, Status Updates"| SPMS
    SPMS -->|"Applicant Rosters, Placement Statistics, CSV Reports"| PO

    SPMS <-->|"Normalized JPA/JDBC Queries & Persistence"| DB
```

### Level 1: Detailed Data Flow Diagram
```mermaid
flowchart TD
    S["Student"]
    PO["Placement Officer"]

    subgraph Processes
        P1["1.0 Authentication & RBAC"]
        P2["2.0 Student Profile Management"]
        P3["3.0 Job Posting Management"]
        P4["4.0 Eligibility Engine & Application"]
        P5["5.0 Applicant Evaluation Pipeline"]
        P6["6.0 Analytics & CSV Reporting"]
    end

    subgraph Data Stores
        D1[("Users Store")]
        D2[("Profiles Store")]
        D3[("Jobs & Companies Store")]
        D4[("Applications Store")]
        D5[("Announcements Store")]
    end

    S -->|"Register / Login"| P1
    PO -->|"Login Credentials"| P1
    P1 <-->|"Verify & Hash"| D1

    S -->|"Profile updates & Resume"| P2
    P2 <-->|"Save Profile & Score"| D2

    PO -->|"Create/Publish Drives"| P3
    P3 <-->|"Store Drives"| D3

    S -->|"Apply to Job"| P4
    P4 <-->|"Check CGPA, Branch, Deadline"| D2
    P4 <-->|"Verify Job Criteria"| D3
    P4 -->|"Store Application (uk constraint)"| D4

    PO -->|"Update Status (Shortlist/Select/Reject)"| P5
    P5 <-->|"Read/Write Application State"| D4

    PO -->|"Request Reports & CSV Export"| P6
    P6 <-->|"Aggregate SQL Stats"| D4
    P6 <-->|"Branch & Year Stats"| D2
```

---

## 3. Layered MVC Architecture Diagram

```mermaid
graph TB
    subgraph Presentation_Layer["Presentation Layer (Thymeleaf & Static Assets)"]
        UI["Bootstrap 5 + Icons + HTML5 + Chart.js"]
        Templates["Thymeleaf Server-Rendered Views (.html)"]
    end

    subgraph Security_Layer["Spring Security 6 (RBAC & Protection)"]
        FilterChain["SecurityFilterChain & BCrypt"]
        AuthHandler["Role-Based Success Redirection"]
    end

    subgraph Controller_Layer["Controller Layer (Spring MVC)"]
        AuthController["AuthController"]
        HomeController["HomeController"]
        StudentController["StudentController"]
        JobController["JobController"]
        AdminController["AdminController & AdminJob/Company/Student Controllers"]
    end

    subgraph Service_Layer["Service Layer (Business Logic & Transactions)"]
        UserService["UserService"]
        ProfileService["StudentProfileService (Resume Validation)"]
        JobService["JobPostingService (Eligibility Engine)"]
        AppService["JobApplicationService (State Machine & Unique Guard)"]
        ReportService["ReportService (Aggregations & CSV Generator)"]
    end

    subgraph Persistence_Layer["Persistence Layer (Spring Data JPA & Hibernate)"]
        Repos["Spring Data JPA Repositories"]
        Hibernate["Hibernate ORM 6.x / JDBC Connection Pool (HikariCP)"]
    end

    subgraph Database_Layer["Relational Database Layer"]
        MySQL[("MySQL 8.0+ Database / H2 Dev Fallback")]
    end

    UI --> FilterChain
    FilterChain --> Templates
    Templates --> Controller_Layer
    Controller_Layer --> Service_Layer
    Service_Layer --> Persistence_Layer
    Persistence_Layer --> Database_Layer
```
