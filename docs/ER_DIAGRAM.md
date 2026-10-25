# Entity-Relationship (ER) Diagram & Database Dictionary

## 1. Overview
The **Student Placement Management System (SPMS)** uses a normalized relational database schema (3NF) designed for high integrity, fast indexing, and strict role segregation.

Key design highlights:
- **Authentication & Authorization Separated from Profile**: The `users` table handles credentials (BCrypt hashed) and security authorities (`ROLE_STUDENT`, `ROLE_PLACEMENT_OFFICER`), while `student_profiles` stores academic and placement-specific data.
- **Unique Application Constraint**: The `job_applications` table enforces a composite unique constraint `(student_id, job_posting_id)` to prevent duplicate applications at the database level.
- **Foreign Key Cascades & Indexes**: Indexes are placed on search columns (`email`, `roll_number`, `branch`, `cgpa`, `status`, `application_deadline`, `priority`).

---

## 2. Mermaid Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    USER ||--o| STUDENT_PROFILE : "has one"
    COMPANY ||--o{ JOB_POSTING : "offers"
    STUDENT_PROFILE ||--o{ JOB_APPLICATION : "submits"
    JOB_POSTING ||--o{ JOB_APPLICATION : "receives"
    JOB_POSTING ||--o{ INTERVIEW_SCHEDULE : "schedules"

    USER {
        bigint id PK
        varchar email UK "Unique college email"
        varchar password "BCrypt encoded hash"
        varchar role "STUDENT or PLACEMENT_OFFICER"
        boolean active "Account status"
        datetime created_at
        datetime updated_at
    }

    STUDENT_PROFILE {
        bigint id PK
        bigint user_id FK,UK "One-to-one link to User"
        varchar full_name
        varchar roll_number UK "Unique student ID"
        varchar phone
        varchar department
        varchar branch "e.g. CSE, ECE, IT, ME"
        int graduation_year "e.g. 2025"
        double cgpa "Scale 10.00"
        double tenth_percentage
        double twelfth_percentage
        text skills "Comma-separated technical tags"
        varchar resume_file_name
        varchar resume_content_type
        longblob resume_data "Encrypted/safe binary blob"
        text address
        varchar gender
        date date_of_birth
        boolean profile_complete
        datetime created_at
        datetime updated_at
    }

    COMPANY {
        bigint id PK
        varchar name UK "Unique corporate recruiter name"
        text description
        varchar website
        varchar industry
        varchar location
        varchar contact_person
        varchar contact_email
        varchar contact_phone
        boolean active
        datetime created_at
        datetime updated_at
    }

    JOB_POSTING {
        bigint id PK
        bigint company_id FK
        varchar title
        varchar job_code UK "Unique identifier"
        text description
        varchar job_type "FULL_TIME, INTERNSHIP, INTERN_PLUS_FULLTIME"
        text required_skills
        varchar location
        double salary_package_lpa "Package in Lakhs Per Annum"
        double minimum_cgpa "Cutoff criteria"
        varchar eligible_branches "Comma-separated or ALL"
        int graduation_year "Target batch"
        int vacancies
        datetime application_deadline
        text selection_process
        varchar status "OPEN, CLOSED, ARCHIVED"
        boolean active
        datetime created_at
        datetime updated_at
    }

    JOB_APPLICATION {
        bigint id PK
        bigint student_id FK
        bigint job_posting_id FK
        varchar status "APPLIED, SHORTLISTED, SELECTED, REJECTED, WITHDRAWN"
        text officer_notes
        text student_notes
        int interview_rounds_cleared
        datetime application_date
        datetime last_status_update
    }

    ANNOUNCEMENT {
        bigint id PK
        varchar title
        text content
        varchar target_audience "ALL, CSE, FINAL_YEAR"
        varchar priority "NORMAL, HIGH, URGENT"
        varchar author_name
        boolean active
        datetime published_at
        datetime updated_at
    }

    INTERVIEW_SCHEDULE {
        bigint id PK
        bigint job_posting_id FK
        varchar round_name
        datetime schedule_date_time
        varchar venue_or_link
        text instructions
        boolean active
        datetime created_at
    }
```

---

## 3. Data Dictionary

### Table: `users`
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE, INDEX | Login username |
| `password` | VARCHAR(255) | NOT NULL | BCrypt hashed password (10 rounds) |
| `role` | VARCHAR(30) | NOT NULL, INDEX | Security role (`STUDENT`, `PLACEMENT_OFFICER`) |
| `active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Account lock/active flag |
| `created_at` | DATETIME | NOT NULL | Account creation timestamp |
| `updated_at` | DATETIME | NULL | Timestamp of last credentials update |

### Table: `student_profiles`
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Profile record identifier |
| `user_id` | BIGINT | NOT NULL, UNIQUE, FK -> users(id) | Associated user account |
| `full_name` | VARCHAR(120) | NOT NULL | Legal name of student |
| `roll_number` | VARCHAR(30) | NOT NULL, UNIQUE, INDEX | Institutional roll / enrollment number |
| `branch` | VARCHAR(50) | NOT NULL, INDEX | Discipline (e.g., CSE, ECE, IT, ME) |
| `graduation_year`| INT | NOT NULL, INDEX | Expected graduation year |
| `cgpa` | DOUBLE | NOT NULL, INDEX | Cumulative Grade Point Average (0.0 to 10.0) |
| `skills` | TEXT | NULL | Technical skill set tags |
| `resume_file_name`| VARCHAR(255) | NULL | Uploaded filename |
| `resume_data` | LONGBLOB | NULL | Uploaded binary resume content |
| `profile_complete`| BOOLEAN | NOT NULL, DEFAULT FALSE | Computed completeness indicator |

### Table: `job_applications`
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Application identifier |
| `student_id` | BIGINT | NOT NULL, FK -> student_profiles(id) | Applicant ID |
| `job_posting_id`| BIGINT | NOT NULL, FK -> job_postings(id) | Job drive ID |
| `status` | VARCHAR(30) | NOT NULL, INDEX | Lifecycle state (`APPLIED`, `SHORTLISTED`, `SELECTED`, `REJECTED`, `WITHDRAWN`) |
| `interview_rounds_cleared`| INT | NOT NULL, DEFAULT 0 | Count of recruitment rounds cleared |
| `officer_notes`| TEXT | NULL | Placement officer evaluation feedback |
| `composite uk` | UNIQUE(`student_id`, `job_posting_id`) | Ensures single application per drive |
