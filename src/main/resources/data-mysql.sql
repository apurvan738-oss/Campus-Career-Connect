-- ===================================================================
-- DRIEMS CAMPUS PLACEMENT - Initial Development Data (MySQL 8.0+)
-- ===================================================================

USE `placement_db`;

-- 1. Placement Officers (Password: Admin@123 -> BCrypt hash)
INSERT INTO `users` (`id`, `email`, `password`, `role`, `active`, `created_at`) VALUES
(1, 'officer@driems.ac.in', '$2a$10$wE6v9q8V4d8q0zS5YqgG0uW5I1U9I6Qk5mF6B1Q4T5w2G8M8x5g1y', 'PLACEMENT_OFFICER', 1, NOW()),
(2, 'officer@college.edu', '$2a$10$wE6v9q8V4d8q0zS5YqgG0uW5I1U9I6Qk5mF6B1Q4T5w2G8M8x5g1y', 'PLACEMENT_OFFICER', 1, NOW())
ON DUPLICATE KEY UPDATE `email`=`email`;

-- 2. Partner Recruiter Companies
INSERT INTO `companies` (`id`, `name`, `industry`, `location`, `website`, `description`, `contact_person`, `contact_email`, `contact_phone`, `active`, `created_at`) VALUES
(1, 'Tata Consultancy Services (TCS)', 'IT Services & Consulting', 'Bhubaneswar / Pan-India', 'https://www.tcs.com', 'Global leader in IT services and consulting.', 'TCS Campus Talent Acquisition', 'campus.recruitment@tcs.com', '0674-2580001', 1, NOW()),
(2, 'Infosys', 'IT Services & Consulting', 'Bhubaneswar / Bangalore', 'https://www.infosys.com', 'Pioneer in digital services and consulting.', 'Campus Connect SPOC', 'talent-acquisition@infosys.com', '0674-2580002', 1, NOW()),
(3, 'Wipro', 'Information Technology & Digital', 'Bhubaneswar / Hyderabad', 'https://www.wipro.com', 'Technology services and enterprise consulting.', 'Campus Hiring Team', 'manager.campus@wipro.com', '0674-2580003', 1, NOW()),
(4, 'Accenture', 'Strategy, Consulting & Technology', 'Kolkata / Bangalore', 'https://www.accenture.com', 'Global professional services and cloud digital leader.', 'University Relations Lead', 'campus.india@accenture.com', '0674-2580004', 1, NOW()),
(5, 'IBM', 'Cloud & Cognitive Software', 'Kolkata / Bangalore', 'https://www.ibm.com', 'Global technology, AI, and hybrid cloud innovator.', 'University Programs Team', 'university-hiring@ibm.com', '0674-2580005', 1, NOW()),
(6, 'Cognizant', 'Information Technology & Outsourcing', 'Kolkata / Chennai', 'https://www.cognizant.com', 'Engineering modern business and technology operations.', 'Campus Lead - East Region', 'campuslead@cognizant.com', '0674-2580006', 1, NOW()),
(7, 'Capgemini', 'IT Consulting & Services', 'Kolkata / Mumbai', 'https://www.capgemini.com', 'Partner in business and technology transformation.', 'Recruitment Operations', 'recruitment.campus@capgemini.com', '0674-2580007', 1, NOW()),
(8, 'Hexaware Technologies', 'Information Technology & Automation', 'Chennai / Pune', 'https://hexaware.com', 'Next-generation IT, automation, and consulting services.', 'Talent Acquisition Manager', 'campus@hexaware.com', '0674-2580008', 1, NOW()),
(9, 'Persistent Systems', 'Digital Engineering & Modernization', 'Pune / Hyderabad', 'https://www.persistent.com', 'Global software and digital engineering leader.', 'University Hiring Coordinator', 'campus-persistent@persistent.com', '0674-2580009', 1, NOW()),
(10, 'Bharat Forge', 'Automotive & Core Engineering', 'Pune / Jamshedpur', 'https://www.bharatforge.com', 'Flagship forging and precision engineering conglomerate.', 'HR Head - Core Recruitment', 'careers.core@bharatforge.com', '0674-2580010', 1, NOW()),
(11, 'Reliance Industries', 'Energy, Telecom & Digital', 'Mumbai / Pan-India', 'https://www.ril.com', 'India’s largest corporation spanning Jio and energy.', 'Campus Recruiter - Jio / RIL', 'ril.campus@ril.com', '0674-2580011', 1, NOW()),
(12, 'Directi', 'Product & Internet Technologies', 'Mumbai / Bangalore', 'https://www.directi.com', 'High-scale consumer web technology conglomerate.', 'Tech Talent Partner', 'jobs@directi.com', '0674-2580012', 1, NOW()),
(13, 'CMS', 'IT Services & Financial Solutions', 'Mumbai / Bhubaneswar', 'https://www.cms.com', 'Enterprise system integration and cash logistics.', 'Talent Executive', 'recruitment@cms.com', '0674-2580013', 1, NOW()),
(14, 'Justdial', 'Local Search Engine & Digital Services', 'Bhubaneswar / Mumbai', 'https://www.justdial.com', 'Leading local search and B2B/B2C listings platform.', 'Regional HR Manager', 'campus.east@justdial.com', '0674-2580014', 1, NOW()),
(15, 'Tata Power', 'Power & Renewable Energy', 'Cuttack / Bhubaneswar', 'https://www.tatapower.com', 'Integrated power distribution and clean solar energy.', 'Tata Power HR Lead', 'campus.odisha@tatapower.com', '0674-2580015', 1, NOW()),
(16, 'India Bulls', 'Real Estate & Financial Services', 'Gurgaon / Mumbai', 'https://www.indiabulls.com', 'Leading real estate and financial advisory group.', 'Corporate HR SPOC', 'careers@indiabulls.com', '0674-2580016', 1, NOW()),
(17, 'Kotak Securities', 'Banking & Capital Markets', 'Mumbai / Kolkata', 'https://www.kotaksecurities.com', 'Capital market broking and equity research firm.', 'Campus Acquisition Specialist', 'campus@kotak.com', '0674-2580017', 1, NOW()),
(18, 'Tejas Constructions', 'Infrastructure & Civil Engineering', 'Bhubaneswar / Cuttack', 'https://tejasconstructions.com', 'Civil infrastructure and smart corridor development.', 'Chief Project Engineer', 'careers@tejasconstructions.com', '0674-2580018', 1, NOW())
ON DUPLICATE KEY UPDATE `name`=`name`;
