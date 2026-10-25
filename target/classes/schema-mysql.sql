-- ===================================================================
-- DRIEMS CAMPUS PLACEMENT - Database Schema (MySQL 8.0+)
-- ===================================================================

CREATE DATABASE IF NOT EXISTS `placement_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `placement_db`;

-- Drop existing tables in reverse dependency order
DROP TABLE IF EXISTS `interview_schedules`;
DROP TABLE IF EXISTS `job_applications`;
DROP TABLE IF EXISTS `job_postings`;
DROP TABLE IF EXISTS `companies`;
DROP TABLE IF EXISTS `student_profiles`;
DROP TABLE IF EXISTS `announcements`;
DROP TABLE IF EXISTS `users`;

-- 1. Users Table (Authentication & Role Authorization)
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(30) NOT NULL,
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    INDEX `idx_user_email` (`email`),
    INDEX `idx_user_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Student Profiles Table (Academic records, CGPA, verified resumes)
CREATE TABLE `student_profiles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `full_name` VARCHAR(120) NOT NULL,
    `roll_number` VARCHAR(30) NOT NULL UNIQUE,
    `phone` VARCHAR(20) NULL,
    `department` VARCHAR(100) NULL,
    `branch` VARCHAR(50) NOT NULL,
    `graduation_year` INT NOT NULL,
    `cgpa` DOUBLE NOT NULL,
    `tenth_percentage` DOUBLE NULL,
    `twelfth_percentage` DOUBLE NULL,
    `skills` TEXT NULL,
    `resume_file_name` VARCHAR(255) NULL,
    `resume_content_type` VARCHAR(100) NULL,
    `resume_data` LONGBLOB NULL,
    `address` TEXT NULL,
    `gender` VARCHAR(20) NULL,
    `date_of_birth` DATE NULL,
    `profile_complete` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    INDEX `idx_student_roll` (`roll_number`),
    INDEX `idx_student_branch` (`branch`),
    INDEX `idx_student_grad_year` (`graduation_year`),
    INDEX `idx_student_cgpa` (`cgpa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Companies Table (Corporate recruitment partners)
CREATE TABLE `companies` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(150) NOT NULL UNIQUE,
    `description` TEXT NULL,
    `website` VARCHAR(200) NULL,
    `industry` VARCHAR(100) NULL,
    `location` VARCHAR(150) NULL,
    `contact_person` VARCHAR(100) NULL,
    `contact_email` VARCHAR(100) NULL,
    `contact_phone` VARCHAR(20) NULL,
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    INDEX `idx_company_name` (`name`),
    INDEX `idx_company_industry` (`industry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Job Postings Table (Placement drives & criteria)
CREATE TABLE `job_postings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `company_id` BIGINT NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `job_code` VARCHAR(50) NOT NULL UNIQUE,
    `description` TEXT NOT NULL,
    `job_type` VARCHAR(30) NOT NULL DEFAULT 'FULL_TIME',
    `required_skills` TEXT NULL,
    `location` VARCHAR(150) NULL,
    `salary_package_lpa` DOUBLE NOT NULL,
    `minimum_cgpa` DOUBLE NOT NULL DEFAULT 6.0,
    `eligible_branches` VARCHAR(200) NOT NULL DEFAULT 'ALL',
    `graduation_year` INT NULL,
    `vacancies` INT NOT NULL DEFAULT 1,
    `application_deadline` DATETIME NOT NULL,
    `selection_process` TEXT NULL,
    `status` VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    CONSTRAINT `fk_job_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE,
    INDEX `idx_job_company` (`company_id`),
    INDEX `idx_job_status` (`status`),
    INDEX `idx_job_deadline` (`application_deadline`),
    INDEX `idx_job_min_cgpa` (`minimum_cgpa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Job Applications Table (Unique constraint prevents duplicate applications)
CREATE TABLE `job_applications` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `job_posting_id` BIGINT NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    `officer_notes` TEXT NULL,
    `student_notes` TEXT NULL,
    `interview_rounds_cleared` INT NOT NULL DEFAULT 0,
    `application_date` DATETIME NOT NULL,
    `last_status_update` DATETIME NULL,
    CONSTRAINT `fk_app_student` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_app_job` FOREIGN KEY (`job_posting_id`) REFERENCES `job_postings` (`id`) ON DELETE CASCADE,
    CONSTRAINT `uk_student_job_application` UNIQUE (`student_id`, `job_posting_id`),
    INDEX `idx_application_student` (`student_id`),
    INDEX `idx_application_job` (`job_posting_id`),
    INDEX `idx_application_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Announcements Table (Notice board circulars)
CREATE TABLE `announcements` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `content` TEXT NOT NULL,
    `target_audience` VARCHAR(100) NOT NULL DEFAULT 'ALL',
    `priority` VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    `author_name` VARCHAR(100) NOT NULL DEFAULT 'Placement Cell',
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `published_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    INDEX `idx_announcement_date` (`published_at`),
    INDEX `idx_announcement_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Interview Schedules Table (Upcoming drive rounds)
CREATE TABLE `interview_schedules` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `job_posting_id` BIGINT NOT NULL,
    `round_name` VARCHAR(100) NOT NULL,
    `schedule_date_time` DATETIME NOT NULL,
    `venue_or_link` VARCHAR(255) NULL,
    `instructions` TEXT NULL,
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL,
    CONSTRAINT `fk_schedule_job` FOREIGN KEY (`job_posting_id`) REFERENCES `job_postings` (`id`) ON DELETE CASCADE,
    INDEX `idx_interview_job` (`job_posting_id`),
    INDEX `idx_interview_time` (`schedule_date_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
