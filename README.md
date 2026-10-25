# DRIEMS CAMPUS PLACEMENT
### Official Training & Placement Automation Portal - DRIEMS
### College Placement & Campus Recruitment Automation Portal
**Built with Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, MySQL, and Thymeleaf**

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Security](https://img.shields.io/badge/Spring%20Security-6.x-red.svg)](https://spring.io/projects/spring-security)
[![Database](https://img.shields.io/badge/Database-MySQL%20%7C%20H2-orange.svg)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Frontend-Thymeleaf%20%2B%20Bootstrap%205-navy.svg)](https://www.thymeleaf.org/)

---

## 1. Project Overview & Purpose
The **DRIEMS CAMPUS PLACEMENT** portal is a comprehensive full-stack web application designed for engineering and management colleges. It centralizes and automates the entire placement lifecycle:
- **Students** can maintain their verified academic profile, check real-time eligibility against company requirements, apply with a single click, upload resumes safely, and track selection rounds up to offer letter release.
- **Placement Officers (TPO / Admins)** manage recruiter companies, publish job drives with CGPA and branch cutoffs, review and filter applicants, record interview stage progressions, broadcast notices, and generate dynamic placement analytics with CSV export.

---

## 2. Technology Stack & Architectural Decisions

| Layer | Technology | Architectural Role |
|---|---|---|
| **Programming Language** | Java 17 LTS | Modern language features (Records, Switch expressions, Sealed types). |
| **Backend Framework** | Spring Boot 3.3.4 | Core framework, dependency injection, embedded Tomcat servlet container. |
| **Web MVC & Templating** | Spring MVC + Thymeleaf 3.1 | Server-side rendering (SSR), clean template layouts, and Spring Security dialect. |
| **Security & Auth** | Spring Security 6 | BCrypt password hashing, Role-Based Access Control (RBAC), CSRF protection. |
| **Data Persistence** | Spring Data JPA & Hibernate 6 | Layered ORM persistence, automatic schema synchronization, parameterized queries. |
| **Database** | MySQL 8.0+ / H2 (dev/test fallback) | Relational database with composite unique constraints and foreign keys. |
| **Validation** | Jakarta Bean Validation | Server-side input normalization and error reporting (`@Valid`, `@NotBlank`, etc.). |
| **Styling & UI** | HTML5, CSS3, Bootstrap 5.3, Bootstrap Icons, Chart.js | Modern navy-blue corporate theme, responsive layouts, live interactive charts. |
| **Automated Testing** | JUnit 5 + Mockito + Spring Test | Unit & integration tests for eligibility checking, duplicate prevention, and RBAC. |

---

## 3. Clear Explanation: JDBC via Spring Data JPA and Hibernate
A common viva and technical question is: *How is JDBC used in this application if we are using Spring Data JPA?*

### The Architectural Reality:
1. **JDBC (Java Database Connectivity)** is the fundamental low-level API provided by Java to establish connections, send SQL queries, and retrieve `ResultSet` records from the relational database.
2. **Hibernate ORM** is built directly on top of JDBC. Rather than writing raw `PreparedStatement` and `ResultSet` mapping code manually, Hibernate handles Object-Relational Mapping (ORM), SQL generation, and type conversions.
3. **Spring Data JPA** sits on top of Hibernate, providing repository abstractions (`JpaRepository`).
4. **HikariCP** (Spring Boot's default high-performance connection pool) manages the physical JDBC connections (`java.sql.Connection`) to MySQL.

```
+-------------------------------------------------------------+
| Application Code (Controllers / Services / Entities / DTOs) |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
| Spring Data JPA (JpaRepository abstractions, query methods) |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
| Hibernate ORM (Entity Lifecycle, Caching, SQL Generation)   |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
| JDBC Driver (MySQL Connector/J) & HikariCP Connection Pool  |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
| MySQL 8.0 Database (Tables, Indexes, Foreign Keys)          |
+-------------------------------------------------------------+
```
**Benefits:**
- Eliminates boilerplate JDBC resource closing (`try-with-resources`).
- Prevents SQL Injection through automatic parameterized statements.
- Preserves standard SQL/JPA portability across database engines.

---

## 4. Key Features & Role Matrix

### Student Capabilities
- **Secure Registration & Login:** Enforces `ROLE_STUDENT`. Passwords encrypted via BCrypt.
- **Academic Profile Management:** Maintain Full Name, Roll No, Branch, CGPA, 10th/12th percentages, phone, and technical skills.
- **Profile Completeness Score:** Dynamic progress bar showing completion percentage (0-100%) and missing field reminders.
- **Resume Upload & Download:** Secure upload (.pdf, .doc, .docx up to 5MB) with MIME-type checks and direct download.
- **Job Drive Catalog & Search:** Filter by Keyword, Company, Branch, Minimum Package, and Location.
- **Live Server-Side Eligibility Engine:** Instant visual badge on job cards (Green = Eligible; Red = Ineligible with specific reasons such as low CGPA, branch mismatch, or passed deadline).
- **Single-Click Application & Duplicate Prevention:** Apply once per job drive. Server and database reject duplicate submissions.
- **Application Status Tracking:** Track stages (`APPLIED`, `SHORTLISTED`, `SELECTED`, `REJECTED`, `WITHDRAWN`).
- **Application Withdrawal:** Allows students to withdraw applications while in `APPLIED` status.
- **Notice Board:** View official placement circulars and interview round schedules.

### Placement Officer (Admin) Capabilities
- **Executive Dashboard:** Live metrics for Total Students, Placed Students, Placement Rate (%), Active Recruiters, Open Drives, and Total Applications.
- **Interactive Analytics Charts (Chart.js):**
  - Branch-wise placement distribution.
  - Application funnel breakdown.
  - Company-wise selections.
- **Corporate Recruiter Management (CRUD):** Add, edit, search, and toggle active status for visiting companies.
- **Placement Drive Management (CRUD):** Post drives with title, package in LPA, minimum CGPA cutoff, eligible branches, vacancies, deadline, and selection rounds.
- **Master Applicant Console:** Multi-criteria filter by Drive, Branch, Status, and Student Name/Roll.
- **Candidate Evaluation:** Update candidate status (`SHORTLISTED`, `SELECTED`, `REJECTED`) with interview rounds cleared and officer feedback.
- **Student Registry:** Search and filter student profiles, inspect academic records, download verified resumes, and deactivate accounts.
- **Notice Broadcast:** Publish announcements with priority tags (`URGENT`, `HIGH`, `NORMAL`).
- **Master CSV Reporting:** Export application records and placement summaries with one click.

---

## 5. Directory & Package Structure

```
student-placement-management-system/
├── pom.xml                                   # Maven dependencies & build configuration
├── README.md                                 # Complete documentation
├── docs/
│   ├── ER_DIAGRAM.md                         # Mermaid ER diagram & database dictionary
│   ├── SYSTEM_DIAGRAMS.md                    # Use-case, DFD Level 0/1, MVC architecture
│   └── MINI_PROJECT_REPORT.md                # Comprehensive academic project report
├── src/
│   ├── main/
│   │   ├── java/com/college/placement/
│   │   │   ├── PlacementApplication.java     # Spring Boot application entry point
│   │   │   ├── config/                       # Security, Seed data, Web configuration
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/                   # Spring MVC Web Controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── StudentController.java
│   │   │   │   ├── JobController.java
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AdminCompanyController.java
│   │   │   │   ├── AdminJobController.java
│   │   │   │   ├── AdminApplicationController.java
│   │   │   │   ├── AdminStudentController.java
│   │   │   │   ├── AdminAnnouncementController.java
│   │   │   │   ├── AdminReportController.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── dto/                          # Data Transfer Objects & Validation
│   │   │   │   ├── UserRegistrationDto.java
│   │   │   │   ├── StudentProfileDto.java
│   │   │   │   ├── JobPostingDto.java
│   │   │   │   ├── JobFilterDto.java
│   │   │   │   ├── ApplicationStatusUpdateDto.java
│   │   │   │   ├── EligibilityResultDto.java
│   │   │   │   ├── PlacementStatsDto.java
│   │   │   │   └── PasswordChangeDto.java
│   │   │   ├── entity/                       # JPA Hibernate Entities & Enums
│   │   │   │   ├── User.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── StudentProfile.java
│   │   │   │   ├── Company.java
│   │   │   │   ├── JobPosting.java
│   │   │   │   ├── JobType.java
│   │   │   │   ├── JobStatus.java
│   │   │   │   ├── JobApplication.java
│   │   │   │   ├── ApplicationStatus.java
│   │   │   │   ├── Announcement.java
│   │   │   │   ├── AnnouncementPriority.java
│   │   │   │   └── InterviewSchedule.java
│   │   │   ├── exception/                    # Domain Exception Classes
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── IneligibleApplicationException.java
│   │   │   │   ├── DuplicateApplicationException.java
│   │   │   │   ├── InvalidStatusTransitionException.java
│   │   │   │   └── UnauthorizedAccessException.java
│   │   │   ├── repository/                   # Spring Data JPA Repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── StudentProfileRepository.java
│   │   │   │   ├── CompanyRepository.java
│   │   │   │   ├── JobPostingRepository.java
│   │   │   │   ├── JobApplicationRepository.java
│   │   │   │   ├── AnnouncementRepository.java
│   │   │   │   └── InterviewScheduleRepository.java
│   │   │   └── service/                      # Business Logic Services
│   │   │       ├── CustomUserDetailsService.java
│   │   │       ├── UserService.java
│   │   │       ├── StudentProfileService.java
│   │   │       ├── CompanyService.java
│   │   │       ├── JobPostingService.java
│   │   │       ├── JobApplicationService.java
│   │   │       ├── AnnouncementService.java
│   │   │       ├── InterviewScheduleService.java
│   │   │       └── ReportService.java
│   │   └── resources/
│   │       ├── application.properties        # Main configuration
│   │       ├── application-dev.properties    # H2 embedded dev profile
│   │       ├── application-mysql.properties  # MySQL production profile
│   │       ├── schema.sql                    # MySQL DDL migration
│   │       ├── static/css/custom.css         # Navy-blue styling
│   │       └── templates/                    # Thymeleaf Server-Rendered Views
│   │           ├── index.html
│   │           ├── about.html
│   │           ├── guidelines.html
│   │           ├── announcements.html
│   │           ├── error.html
│   │           ├── fragments/
│   │           │   ├── navbar.html
│   │           │   ├── footer.html
│   │           │   ├── alerts.html
│   │           │   └── officer_sidebar.html
│   │           ├── auth/
│   │           │   ├── login.html
│   │           │   ├── register.html
│   │           │   ├── change-password.html
│   │           │   └── access-denied.html
│   │           ├── student/
│   │           │   ├── dashboard.html
│   │           │   ├── profile.html
│   │           │   └── applications.html
│   │           ├── jobs/
│   │           │   ├── list.html
│   │           │   └── details.html
│   │           └── officer/
│   │               ├── dashboard.html
│   │               ├── companies/
│   │               ├── jobs/
│   │               ├── applications/
│   │               ├── students/
│   │               ├── announcements/
│   │               └── reports/
│   └── test/java/com/college/placement/      # Automated JUnit 5 Test Suites
│       ├── EligibilityCheckTest.java
│       ├── JobApplicationServiceTest.java
│       └── RoleAuthorizationTest.java
```

---

## 6. Installation & Setup Instructions

### Prerequisites
1. **Java 17 LTS:** Verify installation using `java -version`.
2. **Maven 3.8+:** Verify installation using `mvn -version`.
3. **MySQL 8.0+ (Optional for dev, recommended for production):** Running on port 3306.

---

### Step 1: Database Setup (MySQL)
Open your MySQL terminal or MySQL Workbench and run:
```sql
CREATE DATABASE IF NOT EXISTS placement_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
*(Alternatively, execute the full DDL script provided in `src/main/resources/schema.sql`)*.

---

### Step 2: Environment Variables / Configuration
Configure your database connection via environment variables or directly in `src/main/resources/application-mysql.properties`:

| Variable | Default Value | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` (H2 fallback) or `mysql` | Active Spring profile |
| `DB_HOST` | `localhost` | MySQL Server Host |
| `DB_PORT` | `3306` | MySQL Port |
| `DB_NAME` | `placement_db` | Database Name |
| `DB_USERNAME` | `root` | Database Username |
| `DB_PASSWORD` | `password` | Database Password |
| `PORT` | `3000` | Application Web Port |
| `ADMIN_EMAIL` | `officer@college.edu` | Placement Officer Bootstrap Email |
| `ADMIN_PASSWORD` | `Admin@123` | Placement Officer Bootstrap Password |

---

### Step 3: Build & Run the Application

#### Option A: Running with Development Profile (Zero-Config Embedded H2)
Runs immediately without requiring a local MySQL daemon:
```bash
mvn spring-boot:run
```

#### Option B: Running with MySQL Database Profile
To connect to your live MySQL instance:
```bash
SPRING_PROFILES_ACTIVE=mysql DB_USERNAME=root DB_PASSWORD=your_mysql_password mvn spring-boot:run
```

Access the application in your browser at:
`http://localhost:3000` (or `http://localhost:8080` if PORT is overridden).

---

### Step 4: Running Automated Tests
Run the comprehensive JUnit 5 test suite:
```bash
mvn test
```

---

## 7. Pre-Seeded Evaluation & Viva Demonstration Accounts

The application includes sample seed data automatically initialized by `DataInitializer.java` on first startup:

| Account Type | Email | Password | Role / Details |
|---|---|---|---|
| **Placement Officer** | `officer@college.edu` | `Admin@123` | Full admin privileges over companies, drives, applicants, and reports. |
| **Student 1 (Placed)** | `rahul.cse@college.edu` | `Student@123` | Rahul Sharma (CSE, 8.85 CGPA, Selected at Google for 24.5 LPA). |
| **Student 2 (Shortlisted)** | `priya.ece@college.edu` | `Student@123` | Priya Patel (ECE, 7.80 CGPA, Shortlisted at Deloitte). |
| **Student 3 (Applied)** | `amit.me@college.edu` | `Student@123` | Amit Verma (ME, 6.90 CGPA, Applied at Infosys). |
| **Student 4 (Selected)** | `sneha.it@college.edu` | `Student@123` | Sneha Kulkarni (IT, 9.20 CGPA, Selected at Microsoft for 21 LPA). |

---

## 8. Manual Demonstration & Viva Checklist

Follow this sequence to demonstrate all capabilities during project viva or evaluation:

- [ ] **1. Public Portal & Landing Page:**
  - Open `/` to view the modern navy-blue landing page, real-time metrics (Placement Rate, Highest Package), workflow cards, top recruiters, and open opportunities.
- [ ] **2. Student Registration Flow:**
  - Click **Student Register** (`/register`), fill in new student details with branch, CGPA, and contact info.
  - Submit the form: Verify redirect to `/login` with success banner.
- [ ] **3. Student Profile & Resume:**
  - Log in as the new student. View profile completeness meter.
  - Navigate to **My Profile** (`/student/profile`), upload a PDF resume, and verify that the completeness score reaches 100%.
- [ ] **4. Eligibility Engine Demonstration:**
  - Navigate to **Job Drives** (`/jobs`).
  - Notice the green/red status tags on job cards:
    - If student CGPA is 6.5, Google (Cutoff 8.0) displays a red badge detailing: *"Your CGPA (6.50) does not meet the minimum requirement of 8.00"*.
    - High-eligibility jobs display a green badge: *"You are eligible to apply"*.
- [ ] **5. Single-Click Apply & Duplicate Prevention:**
  - Open an eligible job's details page and click **Apply for this Drive**.
  - Submit application. Verify redirect to **My Applications** (`/student/applications`) showing `APPLIED` status.
  - Return to the same job: Verify the button is disabled and replaced by *"Application Submitted"* with status badge.
- [ ] **6. Application Withdrawal:**
  - In **My Applications**, click **Withdraw** on an application in `APPLIED` status. Confirm action. Status updates to `WITHDRAWN`.
- [ ] **7. Placement Officer Dashboard & Analytics:**
  - Log out and log in as `officer@college.edu` / `Admin@123`.
  - View executive KPI cards and interactive Chart.js graphs (Branch-wise Placements, Status Funnel).
- [ ] **8. Company & Job Management:**
  - Go to **Partner Companies** (`/officer/companies`), add a new corporate partner.
  - Go to **Job Drives** (`/officer/jobs`), post a new recruitment drive with custom CGPA and branch restrictions.
- [ ] **9. Applicant Review & Status Pipeline:**
  - Go to **Review Applicants** (`/officer/applications`). Filter by job drive or branch.
  - Click **Update Status** on an applicant: Select `SHORTLISTED` (1 round cleared) or `SELECTED`. Enter feedback notes.
  - Confirm changes update instantly.
- [ ] **10. Master CSV Export:**
  - Click **Export Master CSV** or **Export Filtered CSV**: Download the generated spreadsheet containing all application milestones.
- [ ] **11. Security & RBAC Enforcement:**
  - Attempt accessing `/officer/dashboard` while logged in as a student: Verify redirection to `/access-denied` (HTTP 403).

---

## 9. Troubleshooting & FAQ

**Q: Can I run the project without having MySQL installed on my computer?**  
A: Yes! The project defaults to the `dev` profile using an in-memory/file H2 database with MySQL dialect compatibility. Simply run `mvn spring-boot:run`.

**Q: How do I connect to MySQL?**  
A: Set the environment variable `SPRING_PROFILES_ACTIVE=mysql` and supply `DB_USERNAME` and `DB_PASSWORD`. The application automatically configures HikariCP and Hibernate for MySQL.

**Q: Where are uploaded resumes stored?**  
A: Uploaded resumes are stored securely as binary data (`byte[]`) in the database table `student_profiles` under the `resume_data` column, with file extension and content-type verification to prevent execution of malicious files.


---

## 13. Running in Visual Studio Code (VS Code)

This repository includes pre-configured `.vscode/launch.json` and `.vscode/tasks.json` configuration files for instant setup:

### Method 1: Using VS Code Run & Debug (F5)
1. Open the project folder in **Visual Studio Code**.
2. Install the recommended extensions: **Extension Pack for Java** and **Spring Boot Extension Pack**.
3. Press **F5** or go to **Run and Debug** (`Ctrl+Shift+D` / `Cmd+Shift+D`).
4. Select **`Spring Boot - DRIEMS Campus Placement`** and click Play.
5. Open your browser at `http://localhost:3000`.

### Method 2: Using the Integrated Terminal Run Scripts
- **Linux / macOS / WSL**: Run `./run.sh`
- **Windows (Command Prompt / PowerShell)**: Run `run.bat`
- **Using npm**: Run `npm run dev`

### Method 3: Using VS Code Tasks
- Press `Ctrl+Shift+B` (or `Cmd+Shift+B` on macOS) to run the default build task: `Spring Boot: Run Application`.

---

## 14. Application Limit & Deadline Handling ("When Limit is Over")

The portal enforces comprehensive client and server-side rules when a placement drive reaches its limits:

1. **Deadline Expiration**: If current system time surpasses `applicationDeadline`, the drive displays a red badge `Deadline Over`. Any submission attempt is intercepted on the server with:
   `"Application deadline is over: The registration window expired on <date>."`
2. **Capacity / Limit Reached**: If vacancies or maximum candidate quota is reached, the drive status is marked as `CLOSED`. The drive card updates to `Limit Over / Closed` and hides the apply trigger.
3. **Server-Side Enforcement**: In `JobPostingService.java` and `JobApplicationService.java`, even if a student attempts direct POST submission, the backend strictly rejects it with an `IneligibleApplicationException`.
4. **Visual Feedback**: On the drive details page, a prominent danger banner notifies candidates:
   *"This placement drive is officially closed. The application deadline has expired or the vacancies/application limit has been reached."*
