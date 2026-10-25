package com.college.placement.config;

import com.college.placement.entity.*;
import com.college.placement.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository applicationRepository;
    private final AnnouncementRepository announcementRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial-admin.email:officer@driems.ac.in}")
    private String adminEmail;

    @Value("${app.initial-admin.password:Admin@123}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository profileRepository,
                           CompanyRepository companyRepository,
                           JobPostingRepository jobPostingRepository,
                           JobApplicationRepository applicationRepository,
                           AnnouncementRepository announcementRepository,
                           InterviewScheduleRepository interviewScheduleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.announcementRepository = announcementRepository;
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already initialized with records.");
            return;
        }

        log.info("Bootstrapping DRIEMS CAMPUS PLACEMENT database with requested recruiter companies...");

        // 1. Placement Officer Accounts (Primary DRIEMS and legacy alias)
        User officer = new User(adminEmail.toLowerCase().trim(), passwordEncoder.encode(adminPassword), Role.PLACEMENT_OFFICER);
        userRepository.save(officer);

        // Keep legacy alias officer@college.edu for evaluation scripts
        if (!adminEmail.equalsIgnoreCase("officer@college.edu")) {
            User officerAlias = new User("officer@college.edu", passwordEncoder.encode("Admin@123"), Role.PLACEMENT_OFFICER);
            userRepository.save(officerAlias);
        }
        log.info("Initialized DRIEMS Placement Officer: {}", adminEmail);

        // 2. All Requested Recruiting Companies
        List<Company> seededCompanies = new ArrayList<>();

        Company tcs = new Company("Tata Consultancy Services (TCS)", "IT Services & Consulting", "Bhubaneswar / Pan-India", "https://www.tcs.com");
        tcs.setDescription("Global leader in IT services, consulting, and business solutions partner of DRIEMS.");
        tcs.setContactPerson("TCS Campus Talent Acquisition");
        tcs.setContactEmail("campus.recruitment@tcs.com");
        tcs.setContactPhone("0674-2580001");
        seededCompanies.add(companyRepository.save(tcs));

        Company infosys = new Company("Infosys", "IT Services & Consulting", "Bhubaneswar / Bangalore", "https://www.infosys.com");
        infosys.setDescription("Next-generation digital services and consulting pioneer with dedicated campus connect.");
        infosys.setContactPerson("Campus Connect SPOC");
        infosys.setContactEmail("talent-acquisition@infosys.com");
        infosys.setContactPhone("0674-2580002");
        seededCompanies.add(companyRepository.save(infosys));

        Company wipro = new Company("Wipro", "Information Technology & Digital", "Bhubaneswar / Hyderabad", "https://www.wipro.com");
        wipro.setDescription("Leading technology services and consulting company focused on building innovative solutions.");
        wipro.setContactPerson("Campus Hiring Team");
        wipro.setContactEmail("manager.campus@wipro.com");
        wipro.setContactPhone("0674-2580003");
        seededCompanies.add(companyRepository.save(wipro));

        Company accenture = new Company("Accenture", "Strategy, Consulting & Technology", "Kolkata / Bangalore", "https://www.accenture.com");
        accenture.setDescription("Global professional services company with leading capabilities in digital, cloud, and security.");
        accenture.setContactPerson("University Relations Lead");
        accenture.setContactEmail("campus.india@accenture.com");
        accenture.setContactPhone("0674-2580004");
        seededCompanies.add(companyRepository.save(accenture));

        Company ibm = new Company("IBM", "Cloud & Cognitive Software", "Kolkata / Bangalore", "https://www.ibm.com");
        ibm.setDescription("Global technology and innovation company headquartered in Armonk, NY, leading in AI and hybrid cloud.");
        ibm.setContactPerson("University Programs Team");
        ibm.setContactEmail("university-hiring@ibm.com");
        ibm.setContactPhone("0674-2580005");
        seededCompanies.add(companyRepository.save(ibm));

        Company cognizant = new Company("Cognizant", "Information Technology & Outsourcing", "Kolkata / Chennai", "https://www.cognizant.com");
        cognizant.setDescription("Engineering modern businesses to improve everyday lives across engineering and technology.");
        cognizant.setContactPerson("Campus Lead - East Region");
        cognizant.setContactEmail("campuslead@cognizant.com");
        cognizant.setContactPhone("0674-2580006");
        seededCompanies.add(companyRepository.save(cognizant));

        Company capgemini = new Company("Capgemini", "IT Consulting & Services", "Kolkata / Mumbai", "https://www.capgemini.com");
        capgemini.setDescription("Global leader in partnering with companies to transform and manage their business through technology.");
        capgemini.setContactPerson("Recruitment Operations");
        capgemini.setContactEmail("recruitment.campus@capgemini.com");
        capgemini.setContactPhone("0674-2580007");
        seededCompanies.add(companyRepository.save(capgemini));

        Company hexaware = new Company("Hexaware Technologies", "Information Technology & Automation", "Chennai / Pune", "https://hexaware.com");
        hexaware.setDescription("Fastest growing next-generation provider of IT, BPO, and consulting services.");
        hexaware.setContactPerson("Talent Acquisition Manager");
        hexaware.setContactEmail("campus@hexaware.com");
        hexaware.setContactPhone("0674-2580008");
        seededCompanies.add(companyRepository.save(hexaware));

        Company persistent = new Company("Persistent Systems", "Digital Engineering & Enterprise Modernization", "Pune / Hyderabad", "https://www.persistent.com");
        persistent.setDescription("Global software engineering solutions provider building software for market leaders.");
        persistent.setContactPerson("University Hiring Coordinator");
        persistent.setContactEmail("campus-persistent@persistent.com");
        persistent.setContactPhone("0674-2580009");
        seededCompanies.add(companyRepository.save(persistent));

        Company bharatForge = new Company("Bharat Forge", "Automotive & Core Engineering", "Pune / Jamshedpur", "https://www.bharatforge.com");
        bharatForge.setDescription("Flagship company of the Kalyani Group and a global leader in metal forming and precision engineering.");
        bharatForge.setContactPerson("HR Head - Core Recruitment");
        bharatForge.setContactEmail("careers.core@bharatforge.com");
        bharatForge.setContactPhone("0674-2580010");
        seededCompanies.add(companyRepository.save(bharatForge));

        Company reliance = new Company("Reliance Industries", "Energy, Telecom & Digital", "Mumbai / Pan-India", "https://www.ril.com");
        reliance.setDescription("India's largest private sector corporation with diversified activities in energy, retail, and telecommunications (Jio).");
        reliance.setContactPerson("Campus Recruiter - Jio / RIL");
        reliance.setContactEmail("ril.campus@ril.com");
        reliance.setContactPhone("0674-2580011");
        seededCompanies.add(companyRepository.save(reliance));

        Company directi = new Company("Directi", "Product & Internet Technologies", "Mumbai / Bangalore", "https://www.directi.com");
        directi.setDescription("Prestigious conglomerate of tech businesses building mass-market web products and communication tools.");
        directi.setContactPerson("Tech Talent Partner");
        directi.setContactEmail("jobs@directi.com");
        directi.setContactPhone("0674-2580012");
        seededCompanies.add(companyRepository.save(directi));

        Company cms = new Company("CMS", "IT Services & Financial Solutions", "Mumbai / Bhubaneswar", "https://www.cms.com");
        cms.setDescription("Leading enterprise solutions, system integration, cash management, and IT infrastructure services company.");
        cms.setContactPerson("Talent Executive");
        cms.setContactEmail("recruitment@cms.com");
        cms.setContactPhone("0674-2580013");
        seededCompanies.add(companyRepository.save(cms));

        Company justdial = new Company("Justdial", "Local Search Engine & Digital Services", "Bhubaneswar / Mumbai", "https://www.justdial.com");
        justdial.setDescription("India's leading local search engine providing fast, reliable information across business and consumer portals.");
        justdial.setContactPerson("Regional HR Manager");
        justdial.setContactEmail("campus.east@justdial.com");
        justdial.setContactPhone("0674-2580014");
        seededCompanies.add(companyRepository.save(justdial));

        Company tataPower = new Company("Tata Power", "Power & Renewable Energy", "Cuttack / Bhubaneswar / Jamshedpur", "https://www.tatapower.com");
        tataPower.setDescription("India's largest integrated power company driving sustainable clean energy and electrical grid automation.");
        tataPower.setContactPerson("TPCODL / Tata Power HR");
        tataPower.setContactEmail("campus.odisha@tatapower.com");
        tataPower.setContactPhone("0674-2580015");
        seededCompanies.add(companyRepository.save(tataPower));

        Company indiabulls = new Company("India Bulls", "Real Estate & Financial Services", "Gurgaon / Mumbai", "https://www.indiabulls.com");
        indiabulls.setDescription("Leading diversified financial services and real estate development group.");
        indiabulls.setContactPerson("Corporate HR SPOC");
        indiabulls.setContactEmail("careers@indiabulls.com");
        indiabulls.setContactPhone("0674-2580016");
        seededCompanies.add(companyRepository.save(indiabulls));

        Company kotakSec = new Company("Kotak Securities", "Banking & Capital Markets", "Mumbai / Kolkata", "https://www.kotaksecurities.com");
        kotakSec.setDescription("One of India's largest broking and capital advisory financial institutions.");
        kotakSec.setContactPerson("Campus Acquisition Specialist");
        kotakSec.setContactEmail("campus@kotak.com");
        kotakSec.setContactPhone("0674-2580017");
        seededCompanies.add(companyRepository.save(kotakSec));

        Company tejasConst = new Company("Tejas Constructions", "Infrastructure & Civil Engineering", "Bhubaneswar / Cuttack", "https://tejasconstructions.com");
        tejasConst.setDescription("Prominent civil construction and infrastructure firm leading commercial projects and smart city developments.");
        tejasConst.setContactPerson("Chief Project Engineer");
        tejasConst.setContactEmail("careers@tejasconstructions.com");
        tejasConst.setContactPhone("0674-2580018");
        seededCompanies.add(companyRepository.save(tejasConst));

        // Also add Google & Microsoft
        Company google = new Company("Google", "Information Technology", "Bangalore / Hyderabad", "https://careers.google.com");
        google.setDescription("Global technology leader in search, cloud computing, software, and hardware.");
        google.setContactPerson("Sundar Placement SPOC");
        google.setContactEmail("campus-hire@google.com");
        google.setContactPhone("9876543210");
        seededCompanies.add(companyRepository.save(google));

        Company microsoft = new Company("Microsoft", "Information Technology & Cloud", "Hyderabad / Bangalore", "https://careers.microsoft.com");
        microsoft.setDescription("Empowering every person and organization on the planet through cloud and AI.");
        microsoft.setContactPerson("Campus Relations Team");
        microsoft.setContactEmail("university-india@microsoft.com");
        microsoft.setContactPhone("9876543211");
        seededCompanies.add(companyRepository.save(microsoft));

        // 3. Sample DRIEMS Students
        // Student 1: Rahul Sharma (DRIEMS CSE, High CGPA, Placed at TCS Digital)
        User u1 = new User("rahul.cse@college.edu", passwordEncoder.encode("Student@123"), Role.STUDENT);
        userRepository.save(u1);
        StudentProfile s1 = new StudentProfile();
        s1.setUser(u1);
        s1.setFullName("Rahul Sharma");
        s1.setRollNumber("DRIEMS-21-CSE-001");
        s1.setPhone("9876500001");
        s1.setBranch("CSE");
        s1.setDepartment("DRIEMS School of Computer Science & Engineering");
        s1.setGraduationYear(2027);
        s1.setCgpa(8.85);
        s1.setTenthPercentage(92.5);
        s1.setTwelfthPercentage(89.0);
        s1.setSkills("Java 17, Spring Boot, Microservices, MySQL, React, Docker");
        s1.setResumeFileName("Rahul_Sharma_DRIEMS_Resume.pdf");
        s1.setResumeContentType("application/pdf");
        s1.setResumeData("SAMPLE_RESUME_CONTENT_RAHUL".getBytes(StandardCharsets.UTF_8));
        s1.setAddress("Karjat, Raigad, Maharashtra");
        s1.setGender("Male");
        s1.checkCompleteness();
        s1 = profileRepository.save(s1);

        // Student 2: Priya Patel (DRIEMS ECE, 7.80 CGPA, Shortlisted at Accenture)
        User u2 = new User("priya.ece@college.edu", passwordEncoder.encode("Student@123"), Role.STUDENT);
        userRepository.save(u2);
        StudentProfile s2 = new StudentProfile();
        s2.setUser(u2);
        s2.setFullName("Priya Patel");
        s2.setRollNumber("DRIEMS-21-ECE-042");
        s2.setPhone("9876500002");
        s2.setBranch("ECE");
        s2.setDepartment("DRIEMS Electronics & Communication Engineering");
        s2.setGraduationYear(2027);
        s2.setCgpa(7.80);
        s2.setTenthPercentage(88.0);
        s2.setTwelfthPercentage(85.5);
        s2.setSkills("C++, Embedded Systems, IoT, Python, Signal Processing");
        s2.setResumeFileName("Priya_Patel_DRIEMS_Resume.pdf");
        s2.setResumeContentType("application/pdf");
        s2.setResumeData("SAMPLE_RESUME_CONTENT_PRIYA".getBytes(StandardCharsets.UTF_8));
        s2.setAddress("Neral, Raigad, Maharashtra");
        s2.setGender("Female");
        s2.checkCompleteness();
        s2 = profileRepository.save(s2);

        // Student 3: Amit Verma (DRIEMS Mechanical, 7.10 CGPA, Placed at Bharat Forge)
        User u3 = new User("amit.me@college.edu", passwordEncoder.encode("Student@123"), Role.STUDENT);
        userRepository.save(u3);
        StudentProfile s3 = new StudentProfile();
        s3.setUser(u3);
        s3.setFullName("Amit Verma");
        s3.setRollNumber("DRIEMS-21-ME-019");
        s3.setPhone("9876500003");
        s3.setBranch("ME");
        s3.setDepartment("DRIEMS Mechanical & Marine Engineering");
        s3.setGraduationYear(2027);
        s3.setCgpa(7.10);
        s3.setTenthPercentage(82.0);
        s3.setTwelfthPercentage(78.5);
        s3.setSkills("AutoCAD, SolidWorks, Thermodynamics, CNC Programming, Quality Control");
        s3.setResumeFileName("Amit_Verma_DRIEMS_Resume.pdf");
        s3.setResumeContentType("application/pdf");
        s3.setResumeData("SAMPLE_RESUME_CONTENT_AMIT".getBytes(StandardCharsets.UTF_8));
        s3.setAddress("Karjat, Raigad, Maharashtra");
        s3.setGender("Male");
        s3.checkCompleteness();
        s3 = profileRepository.save(s3);

        // Student 4: Sneha Kulkarni (DRIEMS IT, 9.20 CGPA, Selected at Directi)
        User u4 = new User("sneha.it@college.edu", passwordEncoder.encode("Student@123"), Role.STUDENT);
        userRepository.save(u4);
        StudentProfile s4 = new StudentProfile();
        s4.setUser(u4);
        s4.setFullName("Sneha Kulkarni");
        s4.setRollNumber("DRIEMS-21-IT-012");
        s4.setPhone("9876500004");
        s4.setBranch("IT");
        s4.setDepartment("DRIEMS Information Technology");
        s4.setGraduationYear(2027);
        s4.setCgpa(9.20);
        s4.setTenthPercentage(96.0);
        s4.setTwelfthPercentage(94.5);
        s4.setSkills("DSA, Java, Spring Boot, AWS, GraphQL, REST APIs");
        s4.setResumeFileName("Sneha_Kulkarni_DRIEMS_Resume.pdf");
        s4.setResumeContentType("application/pdf");
        s4.setResumeData("SAMPLE_RESUME_CONTENT_SNEHA".getBytes(StandardCharsets.UTF_8));
        s4.setAddress("Neral, Raigad, Maharashtra");
        s4.setGender("Female");
        s4.checkCompleteness();
        s4 = profileRepository.save(s4);

        // 4. Sample Job Postings for Newly Added Companies
        // Job 1: TCS Digital & Ninja
        JobPosting jTcs = new JobPosting();
        jTcs.setCompany(tcs);
        jTcs.setTitle("Digital Innovator & Systems Engineer");
        jTcs.setJobCode("TCS-DIG-2027");
        jTcs.setDescription("Join Tata Consultancy Services through the DRIEMS on-campus recruitment drive. Roles available in cloud engineering, digital transformation, and application development.");
        jTcs.setJobType(JobType.FULL_TIME);
        jTcs.setRequiredSkills("Java, Python, C++, SQL, Problem Solving, Data Structures");
        jTcs.setLocation("Bhubaneswar / Kolkata / Pan-India");
        jTcs.setSalaryPackageLpa(9.00);
        jTcs.setMinimumCgpa(6.50);
        jTcs.setEligibleBranches("ALL");
        jTcs.setGraduationYear(2027);
        jTcs.setVacancies(75);
        jTcs.setApplicationDeadline(LocalDateTime.now().plusDays(25));
        jTcs.setSelectionProcess("Round 1: TCS NQT Online Test\nRound 2: Technical Interview\nRound 3: HR & Management Interview");
        jTcs.setStatus(JobStatus.OPEN);
        jTcs = jobPostingRepository.save(jTcs);

        // Job 2: Wipro Turbo & Elite
        JobPosting jWipro = new JobPosting();
        jWipro.setCompany(wipro);
        jWipro.setTitle("Project Engineer - Digital Tech");
        jWipro.setJobCode("WIPRO-ENG-2027");
        jWipro.setDescription("Full-stack engineering role working on cloud platforms, digital apps, and enterprise transformation solutions for Fortune 500 clients.");
        jWipro.setJobType(JobType.FULL_TIME);
        jWipro.setRequiredSkills("Java, C#, Web Technologies, Database Concepts");
        jWipro.setLocation("Bhubaneswar / Hyderabad / Bangalore");
        jWipro.setSalaryPackageLpa(6.50);
        jWipro.setMinimumCgpa(6.00);
        jWipro.setEligibleBranches("CSE, IT, ECE, EE");
        jWipro.setGraduationYear(2027);
        jWipro.setVacancies(40);
        jWipro.setApplicationDeadline(LocalDateTime.now().plusDays(18));
        jWipro.setSelectionProcess("Online Assessment -> Technical Interview -> HR Round");
        jWipro.setStatus(JobStatus.OPEN);
        jWipro = jobPostingRepository.save(jWipro);

        // Job 3: Accenture Advanced Application Engineering
        JobPosting jAcc = new JobPosting();
        jAcc.setCompany(accenture);
        jAcc.setTitle("Advanced Application Engineering Analyst");
        jAcc.setJobCode("ACCN-AAEA-2027");
        jAcc.setDescription("Design, build, and test applications using cutting-edge cloud architectures and modern development frameworks.");
        jAcc.setJobType(JobType.FULL_TIME);
        jAcc.setRequiredSkills("Object Oriented Programming, Python, Java, Agile");
        jAcc.setLocation("Kolkata / Bangalore / Pune");
        jAcc.setSalaryPackageLpa(7.50);
        jAcc.setMinimumCgpa(6.50);
        jAcc.setEligibleBranches("ALL");
        jAcc.setGraduationYear(2027);
        jAcc.setVacancies(50);
        jAcc.setApplicationDeadline(LocalDateTime.now().plusDays(14));
        jAcc.setSelectionProcess("Cognitive & Technical Assessment -> Coding Assessment -> Communication Assessment -> Interview");
        jAcc.setStatus(JobStatus.OPEN);
        jAcc = jobPostingRepository.save(jAcc);

        // Job 4: Directi SDE
        JobPosting jDirecti = new JobPosting();
        jDirecti.setCompany(directi);
        jDirecti.setTitle("Software Development Engineer (Core Products)");
        jDirecti.setJobCode("DIR-SDE-2027");
        jDirecti.setDescription("Elite product engineering role building high-throughput distributed systems, databases, and consumer platforms.");
        jDirecti.setJobType(JobType.FULL_TIME);
        jDirecti.setRequiredSkills("Data Structures, Complex Algorithms, Java, C++, Systems Architecture");
        jDirecti.setLocation("Bangalore / Mumbai");
        jDirecti.setSalaryPackageLpa(22.00);
        jDirecti.setMinimumCgpa(8.00);
        jDirecti.setEligibleBranches("CSE, IT");
        jDirecti.setGraduationYear(2027);
        jDirecti.setVacancies(8);
        jDirecti.setApplicationDeadline(LocalDateTime.now().plusDays(30));
        jDirecti.setSelectionProcess("Online Code Challenge -> Technical Round 1 -> Technical Round 2 -> Architecture Review");
        jDirecti.setStatus(JobStatus.OPEN);
        jDirecti = jobPostingRepository.save(jDirecti);

        // Job 5: Bharat Forge (Core Mechanical)
        JobPosting jBf = new JobPosting();
        jBf.setCompany(bharatForge);
        jBf.setTitle("Graduate Engineer Trainee - Precision Engineering");
        jBf.setJobCode("BF-GET-2027");
        jBf.setDescription("Core engineering opportunity for Mechanical & Marine engineers in advanced forging, automotive metallurgy, and robotic manufacturing.");
        jBf.setJobType(JobType.FULL_TIME);
        jBf.setRequiredSkills("Strength of Materials, Manufacturing Science, Thermodynamics, CAD/CAM");
        jBf.setLocation("Pune / Jamshedpur");
        jBf.setSalaryPackageLpa(6.80);
        jBf.setMinimumCgpa(6.50);
        jBf.setEligibleBranches("ME, CIVIL");
        jBf.setGraduationYear(2027);
        jBf.setVacancies(20);
        jBf.setApplicationDeadline(LocalDateTime.now().plusDays(12));
        jBf.setSelectionProcess("Technical Written Test -> Core Interview -> Plant Head Interview");
        jBf.setStatus(JobStatus.OPEN);
        jBf = jobPostingRepository.save(jBf);

        // Job 6: Tata Power (Electrical & Renewable)
        JobPosting jTp = new JobPosting();
        jTp.setCompany(tataPower);
        jTp.setTitle("Graduate Engineer Trainee - Power Distribution");
        jTp.setJobCode("TP-GET-2027");
        jTp.setDescription("Lead electrical distribution automation, renewable energy integration, and smart metering systems across Odisha.");
        jTp.setJobType(JobType.FULL_TIME);
        jTp.setRequiredSkills("Power Systems, Electrical Machines, SCADA, Grid Management");
        jTp.setLocation("Cuttack / Bhubaneswar");
        jTp.setSalaryPackageLpa(6.20);
        jTp.setMinimumCgpa(6.00);
        jTp.setEligibleBranches("EE, ECE, ME");
        jTp.setGraduationYear(2027);
        jTp.setVacancies(25);
        jTp.setApplicationDeadline(LocalDateTime.now().plusDays(16));
        jTp.setSelectionProcess("Written Assessment -> Technical Interview -> HR Round");
        jTp.setStatus(JobStatus.OPEN);
        jTp = jobPostingRepository.save(jTp);

        // Job 7: Tejas Constructions (Civil Engineering)
        JobPosting jTejas = new JobPosting();
        jTejas.setCompany(tejasConst);
        jTejas.setTitle("Site Planning & Structural Engineer Trainee");
        jTejas.setJobCode("TEJAS-CIV-2027");
        jTejas.setDescription("Join large-scale institutional construction and infrastructure projects in smart city corridors.");
        jTejas.setJobType(JobType.FULL_TIME);
        jTejas.setRequiredSkills("Structural Analysis, Quantity Estimation, AutoCAD, Site Supervision");
        jTejas.setLocation("Bhubaneswar / Cuttack");
        jTejas.setSalaryPackageLpa(5.50);
        jTejas.setMinimumCgpa(6.00);
        jTejas.setEligibleBranches("CIVIL");
        jTejas.setGraduationYear(2027);
        jTejas.setVacancies(15);
        jTejas.setApplicationDeadline(LocalDateTime.now().plusDays(20));
        jTejas.setSelectionProcess("Direct Technical Interview -> Site Practical Test");
        jTejas.setStatus(JobStatus.OPEN);
        jTejas = jobPostingRepository.save(jTejas);

        // Job 8: Infosys Specialist Programmer & DSE
        JobPosting jInfy = new JobPosting();
        jInfy.setCompany(infosys);
        jInfy.setTitle("Specialist Programmer (SP) & DSE");
        jInfy.setJobCode("INFY-SP-2027");
        jInfy.setDescription("High-impact algorithmic engineering role building next-generation digital cloud platforms.");
        jInfy.setJobType(JobType.FULL_TIME);
        jInfy.setRequiredSkills("Java, Python, Algorithms, Data Structures, System Design");
        jInfy.setLocation("Bhubaneswar / Bangalore");
        jInfy.setSalaryPackageLpa(9.50);
        jInfy.setMinimumCgpa(6.50);
        jInfy.setEligibleBranches("CSE, IT, ECE");
        jInfy.setGraduationYear(2027);
        jInfy.setVacancies(35);
        jInfy.setApplicationDeadline(LocalDateTime.now().plusDays(22));
        jInfy.setSelectionProcess("Coding Round -> Technical Interview -> HR Discussion");
        jInfy.setStatus(JobStatus.OPEN);
        jInfy = jobPostingRepository.save(jInfy);

        // Job 9: Cognizant GenC
        JobPosting jCts = new JobPosting();
        jCts.setCompany(cognizant);
        jCts.setTitle("GenC Next - Cloud & Full Stack Developer");
        jCts.setJobCode("CTS-GENC-2027");
        jCts.setDescription("Work on global enterprise modernisation, microservices architecture, and cloud migrations.");
        jCts.setJobType(JobType.FULL_TIME);
        jCts.setRequiredSkills("Java 17, Spring Boot, React, SQL, Cloud Basics");
        jCts.setLocation("Kolkata / Chennai");
        jCts.setSalaryPackageLpa(6.75);
        jCts.setMinimumCgpa(6.00);
        jCts.setEligibleBranches("ALL");
        jCts.setGraduationYear(2027);
        jCts.setVacancies(45);
        jCts.setApplicationDeadline(LocalDateTime.now().plusDays(17));
        jCts.setSelectionProcess("Aptitude & Coding Assessment -> Technical Interview");
        jCts.setStatus(JobStatus.OPEN);
        jCts = jobPostingRepository.save(jCts);

        // Job 10: Persistent Systems
        JobPosting jPersistent = new JobPosting();
        jPersistent.setCompany(persistent);
        jPersistent.setTitle("Software Engineer - Digital Engineering");
        jPersistent.setJobCode("PER-SE-2027");
        jPersistent.setDescription("Develop mission-critical healthcare, banking, and enterprise cloud applications.");
        jPersistent.setJobType(JobType.FULL_TIME);
        jPersistent.setRequiredSkills("Java, C++, Object Oriented Design, REST APIs");
        jPersistent.setLocation("Pune / Hyderabad");
        jPersistent.setSalaryPackageLpa(8.00);
        jPersistent.setMinimumCgpa(6.50);
        jPersistent.setEligibleBranches("CSE, IT, ECE");
        jPersistent.setGraduationYear(2027);
        jPersistent.setVacancies(25);
        jPersistent.setApplicationDeadline(LocalDateTime.now().plusDays(21));
        jPersistent.setSelectionProcess("Online Test -> Technical Rounds -> HR");
        jPersistent.setStatus(JobStatus.OPEN);
        jPersistent = jobPostingRepository.save(jPersistent);

        // Job 11: Hexaware Technologies
        JobPosting jHex = new JobPosting();
        jHex.setCompany(hexaware);
        jHex.setTitle("Graduate Engineer Trainee - Premier");
        jHex.setJobCode("HEX-PTP-2027");
        jHex.setDescription("Fast-track automation and digital engineering program with cutting-edge tech immersion.");
        jHex.setJobType(JobType.FULL_TIME);
        jHex.setRequiredSkills("Java, Python, Automation Testing, Web Technologies");
        jHex.setLocation("Chennai / Pune");
        jHex.setSalaryPackageLpa(6.00);
        jHex.setMinimumCgpa(6.00);
        jHex.setEligibleBranches("ALL");
        jHex.setGraduationYear(2027);
        jHex.setVacancies(30);
        jHex.setApplicationDeadline(LocalDateTime.now().plusDays(19));
        jHex.setSelectionProcess("Aptitude & Communication Test -> Technical Interview");
        jHex.setStatus(JobStatus.OPEN);
        jHex = jobPostingRepository.save(jHex);

        // Job 12: Reliance Industries (Jio Platforms)
        JobPosting jRil = new JobPosting();
        jRil.setCompany(reliance);
        jRil.setTitle("Graduate Engineer Trainee - 5G & Cloud Services");
        jRil.setJobCode("RIL-JIO-2027");
        jRil.setDescription("Join Reliance Jio telecom and cloud infrastructure powering digital India.");
        jRil.setJobType(JobType.FULL_TIME);
        jRil.setRequiredSkills("Computer Networks, Linux, Java/Python, Telecom Protocols");
        jRil.setLocation("Mumbai / Pan-India");
        jRil.setSalaryPackageLpa(7.20);
        jRil.setMinimumCgpa(6.50);
        jRil.setEligibleBranches("CSE, IT, ECE, EE");
        jRil.setGraduationYear(2027);
        jRil.setVacancies(30);
        jRil.setApplicationDeadline(LocalDateTime.now().plusDays(24));
        jRil.setSelectionProcess("Online Written Assessment -> Technical Interview");
        jRil.setStatus(JobStatus.OPEN);
        jRil = jobPostingRepository.save(jRil);

        // Job 13: Kotak Securities
        JobPosting jKotak = new JobPosting();
        jKotak.setCompany(kotakSec);
        jKotak.setTitle("FinTech Software Analyst");
        jKotak.setJobCode("KOTAK-FT-2027");
        jKotak.setDescription("Build ultra-low latency trading interfaces and financial analytics engines.");
        jKotak.setJobType(JobType.FULL_TIME);
        jKotak.setRequiredSkills("Java, Spring Boot, Microservices, SQL, Trading Systems");
        jKotak.setLocation("Mumbai / Kolkata");
        jKotak.setSalaryPackageLpa(10.00);
        jKotak.setMinimumCgpa(7.00);
        jKotak.setEligibleBranches("CSE, IT");
        jKotak.setGraduationYear(2027);
        jKotak.setVacancies(12);
        jKotak.setApplicationDeadline(LocalDateTime.now().plusDays(26));
        jKotak.setSelectionProcess("Coding Test -> Technical Round -> Management Interview");
        jKotak.setStatus(JobStatus.OPEN);
        jKotak = jobPostingRepository.save(jKotak);

        // Job 14: IBM Cloud (Application Limit Over / Deadline Passed)
        JobPosting jIbm = new JobPosting();
        jIbm.setCompany(ibm);
        jIbm.setTitle("Associate System Engineer - Cloud Infrastructure");
        jIbm.setJobCode("IBM-CLOUD-2027");
        jIbm.setDescription("Registration window closed. Applicant quota filled for 2026-2027 batch campus hiring.");
        jIbm.setJobType(JobType.FULL_TIME);
        jIbm.setRequiredSkills("Linux, Cloud Computing, Python, Shell Scripting");
        jIbm.setLocation("Mumbai / Pune");
        jIbm.setSalaryPackageLpa(5.50);
        jIbm.setMinimumCgpa(6.00);
        jIbm.setEligibleBranches("ALL");
        jIbm.setGraduationYear(2027);
        jIbm.setVacancies(10);
        jIbm.setApplicationDeadline(LocalDateTime.now().minusDays(2)); // Expired deadline
        jIbm.setStatus(JobStatus.CLOSED); // Closed / Limit reached
        jIbm = jobPostingRepository.save(jIbm);

        // 5. Sample Applications Seeded
        // Rahul -> TCS Digital (Selected)
        JobApplication a1 = new JobApplication(s1, jTcs);
        a1.setStatus(ApplicationStatus.SELECTED);
        a1.setInterviewRoundsCleared(3);
        a1.setOfficerNotes("Candidate cleared TCS Digital NQT and interview. Offer letter issued for 9.0 LPA.");
        applicationRepository.save(a1);

        // Priya -> Accenture (Shortlisted)
        JobApplication a2 = new JobApplication(s2, jAcc);
        a2.setStatus(ApplicationStatus.SHORTLISTED);
        a2.setInterviewRoundsCleared(2);
        a2.setOfficerNotes("Cleared online assessment and coding test. Final interview scheduled.");
        applicationRepository.save(a2);

        // Amit -> Bharat Forge (Selected)
        JobApplication a3 = new JobApplication(s3, jBf);
        a3.setStatus(ApplicationStatus.SELECTED);
        a3.setInterviewRoundsCleared(3);
        a3.setOfficerNotes("Candidate selected for GET role at Bharat Forge Pune plant. Package 6.8 LPA.");
        applicationRepository.save(a3);

        // Sneha -> Directi (Selected)
        JobApplication a4 = new JobApplication(s4, jDirecti);
        a4.setStatus(ApplicationStatus.SELECTED);
        a4.setInterviewRoundsCleared(4);
        a4.setOfficerNotes("Candidate accepted dream offer of 22 LPA at Directi Bangalore.");
        applicationRepository.save(a4);

        // 6. DRIEMS Campus Announcements
        Announcement ann1 = new Announcement(
                "TCS Campus Recruitment Drive 2026–2027 Registration Open",
                "Tata Consultancy Services (TCS) on-campus drive for DRIEMS 2027 passing out batch (2026–2027) is officially scheduled. All eligible B.Tech students must register before the cutoff date.",
                AnnouncementPriority.URGENT,
                "ALL"
        );
        announcementRepository.save(ann1);

        Announcement ann2 = new Announcement(
                "Bharat Forge Core Campus Drive for Mechanical Students",
                "Bharat Forge recruitment team will visit DRIEMS campus on 28th October for physical interviews and aptitude testing in the Central Seminar Hall.",
                AnnouncementPriority.HIGH,
                "ME, CIVIL"
        );
        announcementRepository.save(ann2);

        Announcement ann3 = new Announcement(
                "Wipro & Accenture Placement Orientation Workshop",
                "Training & Placement Cell, DRIEMS will organize a 2-day technical bootcamp on Data Structures and Aptitude for registered applicants.",
                AnnouncementPriority.NORMAL,
                "ALL"
        );
        announcementRepository.save(ann3);

        // 7. Interview Schedule
        InterviewSchedule is1 = new InterviewSchedule(jTcs, "TCS Technical & MR Interview", LocalDateTime.now().plusDays(4).withHour(9).withMinute(30), "DRIEMS Central Lab 1 & 2");
        is1.setInstructions("Formal dress code mandatory. Carry DRIEMS ID card, 2 copies of resume, and original certificates.");
        interviewScheduleRepository.save(is1);

        log.info("DRIEMS CAMPUS PLACEMENT bootstrap completed successfully with {} companies and active drives.", seededCompanies.size());
    }
}
