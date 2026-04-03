-- ==============================================================================
-- HireMind AI - Sample Seed Data for Testing & Development
-- ==============================================================================

-- Disable constraints temporarily for seeding
ALTER TABLE audit_logs DISABLE TRIGGER ALL;
ALTER TABLE notifications DISABLE TRIGGER ALL;

-- ==============================================================================
-- Organizations
-- ==============================================================================
INSERT INTO organizations (id, name, description, website, industry, employee_count, subscription_tier, max_users, max_resumes, max_job_descriptions)
VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'TechCorp Inc', 'Leading technology consulting firm', 'https://techcorp.example.com', 'Technology', 5000, 'ENTERPRISE', 100, 1000, 500),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d480', 'FinanceFirst', 'Premier financial services company', 'https://financefirst.example.com', 'Finance', 2000, 'PROFESSIONAL', 50, 200, 100),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d481', 'Innovation Labs', 'AI and ML research organization', 'https://innovationlabs.example.com', 'Research & Development', 500, 'STARTER', 20, 100, 50);

-- ==============================================================================
-- Users (Test Users)
-- ==============================================================================
INSERT INTO users (id, email, password_hash, full_name, role, organization_id, is_active, email_verified, created_at)
VALUES
    ('a47ac10b-58cc-4372-a567-0e02b2c3d479', 'alice@example.com', '$2a$10$dummy_hash_for_alice_123456789', 'Alice Johnson', 'RECRUITER', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', true, true, '2025-01-15 10:00:00'),
    ('a47ac10b-58cc-4372-a567-0e02b2c3d480', 'bob@example.com', '$2a$10$dummy_hash_for_bob_1234567890', 'Bob Smith', 'USER', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', true, true, '2025-01-16 11:30:00'),
    ('a47ac10b-58cc-4372-a567-0e02b2c3d481', 'carol@example.com', '$2a$10$dummy_hash_for_carol_123456789', 'Carol White', 'USER', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', true, true, '2025-01-17 09:15:00'),
    ('a47ac10b-58cc-4372-a567-0e02b2c3d482', 'admin@example.com', '$2a$10$dummy_hash_for_admin_123456789', 'Admin User', 'ADMIN', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', true, true, '2025-01-01 00:00:00');

-- ==============================================================================
-- Resumes (Sample Resumes)
-- ==============================================================================
INSERT INTO resumes (id, user_id, original_filename, file_path, file_size_bytes, mime_type, parsed_content, skills_extracted, experiences, education, summary, parsing_status)
VALUES
    ('b47ac10b-58cc-4372-a567-0e02b2c3d479', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'Bob_Smith_Resume.pdf', '/uploads/resumes/b47ac10b-58cc-4372-a567-0e02b2c3d479.pdf', 125000, 'application/pdf',
        'Bob Smith | Senior Software Engineer | bob@example.com | (555) 123-4567\n\nEXPERIENCE:\nSenior Software Engineer at TechCorp (2021-Present)\n- Led development of microservices architecture\n- Managed team of 5 engineers\n- Improved system performance by 40%\n\nSoftware Engineer at StartupXYZ (2018-2021)\n- Full-stack development using Java and React\n- Implemented CI/CD pipelines\n\nEDUCATION:\nBS Computer Science - State University (2018)\n\nCERTIFICATIONS:\n- AWS Solutions Architect Associate\n- Certified Kubernetes Administrator\n',
        '["Java", "Spring Boot", "React", "JavaScript", "PostgreSQL", "Kubernetes", "Docker", "AWS", "Microservices", "REST APIs"]',
        '[{"company": "TechCorp", "position": "Senior Software Engineer", "duration": "2021-Present", "skills": ["Java", "Spring Boot", "Kubernetes"]},{"company": "StartupXYZ", "position": "Software Engineer", "duration": "2018-2021", "skills": ["Java", "React", "CI/CD"]}]',
        '[{"institution": "State University", "degree": "BS Computer Science", "year": 2018}]',
        'Experienced senior software engineer with 6+ years of expertise in Java, Spring Boot, and cloud technologies',
        'COMPLETED'),
    ('b47ac10b-58cc-4372-a567-0e02b2c3d480', 'a47ac10b-58cc-4372-a567-0e02b2c3d481', 'Carol_White_Resume.docx', '/uploads/resumes/b47ac10b-58cc-4372-a567-0e02b2c3d480.docx', 95000, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'Carol White | Data Scientist | carol@example.com | (555) 987-6543\n\nEXPERIENCE:\nSenior Data Scientist at FinanceFirst (2020-Present)\n- Developed ML models for credit risk assessment\n- Created data pipelines processing 10B+ records\n- Led analytics team of 3 data scientists\n\nData Analyst at InvestCorp (2018-2020)\n- SQL and Python data analysis\n- Tableau dashboard development\n\nEDUCATION:\nMS Data Science - Tech University (2018)\nBS Mathematics - Tech University (2016)\n',
        '["Python", "SQL", "Machine Learning", "TensorFlow", "Pandas", "Scikit-learn", "Tableau", "Spark", "AWS", "Statistics"]',
        '[{"company": "FinanceFirst", "position": "Senior Data Scientist", "duration": "2020-Present", "skills": ["Python", "Machine Learning", "SQL"]},{"company": "InvestCorp", "position": "Data Analyst", "duration": "2018-2020", "skills": ["SQL", "Python", "Tableau"]}]',
        '[{"institution": "Tech University", "degree": "MS Data Science", "year": 2018},{"institution": "Tech University", "degree": "BS Mathematics", "year": 2016}]',
        'Data-driven professional with 5+ years in ML and analytics, specializing in financial modeling',
        'COMPLETED');

-- ==============================================================================
-- Job Descriptions (Sample Job Postings)
-- ==============================================================================
INSERT INTO job_descriptions (id, user_id, title, company, location, raw_text, parsed_skills, parsed_requirements, employment_type, experience_level, industry, parsing_status, parsed_successfully)
VALUES
    ('c47ac10b-58cc-4372-a567-0e02b2c3d479', 'a47ac10b-58cc-4372-a567-0e02b2c3d479', 'Senior Software Engineer - Backend', 'TechCorp', 'San Francisco, CA',
        'Senior Software Engineer - Backend\nLocation: San Francisco, CA\nCompany: TechCorp Inc\n\nAbout the Role:\nWe are seeking an experienced Senior Software Engineer to join our platform engineering team.\n\nResponsibilities:\n- Design and implement scalable microservices\n- Mentor junior engineers\n- Conduct code reviews\n- Optimize system performance\n\nRequirements:\n- 5+ years of backend development experience\n- Proficiency in Java, Spring Boot\n- Strong understanding of REST APIs\n- Experience with Docker and Kubernetes\n- PostgreSQL database knowledge\n- AWS experience preferred\n\nNice to Have:\n- Kubernetes certification\n- CI/CD pipeline experience\n- Open source contributions\n',
        '["Java", "Spring Boot", "Kubernetes", "Docker", "PostgreSQL", "REST APIs", "Microservices", "AWS"]',
        '{"required": ["5+ years backend development", "Java", "Spring Boot", "Docker", "Kubernetes", "REST APIs"]}',
        'Full-time', 'Senior (5+ years)', 'Technology', true, true),

    ('c47ac10b-58cc-4372-a567-0e02b2c3d480', 'a47ac10b-58cc-4372-a567-0e02b2c3d479', 'Machine Learning Engineer', 'InnovationLabs', 'Mountain View, CA',
        'Machine Learning Engineer\nLocation: Mountain View, CA\nCompany: InnovationLabs\n\nAbout the Role:\nJoin our AI research team to build cutting-edge ML models and systems.\n\nResponsibilities:\n- Develop and deploy ML models\n- Build data pipelines\n- Conduct experiments and research\n- Collaborate with data scientists\n\nRequirements:\n- 3+ years ML/AI experience\n- Python proficiency\n- TensorFlow or PyTorch experience\n- SQL skills\n- Understanding of ML pipelines\n- Statistics and mathematics knowledge\n\nPreferred:\n- PhD in Machine Learning/CS\n- Experience with LLMs\n- Cloud platform experience (AWS, GCP)\n',
        '["Python", "TensorFlow", "Machine Learning", "SQL", "Statistics", "PyTorch", "AWS", "GCP"]',
        '{"required": ["3+ years ML experience", "Python", "TensorFlow or PyTorch", "SQL", "Statistics"]}',
        'Full-time', 'Mid-Level (3-5 years)', 'Research & Development', true, true);

-- ==============================================================================
-- Analysis Results (Sample Analysis)
-- ==============================================================================
INSERT INTO analysis_results (id, user_id, resume_id, job_description_id, overall_score, score_breakdown, matched_skills, missing_skills, ats_issues, suggestions)
VALUES
    ('d47ac10b-58cc-4372-a567-0e02b2c3d479', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'b47ac10b-58cc-4372-a567-0e02b2c3d479', 'c47ac10b-58cc-4372-a567-0e02b2c3d479', 87.50,
        '{"required_skills_match": 35, "preferred_skills_match": 14, "semantic_similarity": 14, "domain_alignment": 9, "ats_optimization": 14, "formatting": 9}',
        '["Java", "Spring Boot", "Kubernetes", "Docker", "PostgreSQL", "REST APIs", "Microservices"]',
        '[]',
        '[{"issue": "Missing AWS keyword", "severity": "LOW"}, {"issue": "Could add more metrics to achievement statements", "severity": "MEDIUM"}]',
        '[{"suggestion": "Add AWS certification to skills section", "priority": "HIGH"}, {"suggestion": "Quantify all achievements with percentages or numbers", "priority": "HIGH"}, {"suggestion": "Add keywords: ''event-driven architecture'', ''distributed systems''", "priority": "MEDIUM"}]'),

    ('d47ac10b-58cc-4372-a567-0e02b2c3d480', 'a47ac10b-58cc-4372-a567-0e02b2c3d481', 'b47ac10b-58cc-4372-a567-0e02b2c3d480', 'c47ac10b-58cc-4372-a567-0e02b2c3d480', 72.30,
        '{"required_skills_match": 25, "preferred_skills_match": 10, "semantic_similarity": 13, "domain_alignment": 8, "ats_optimization": 12, "formatting": 8}',
        '["Python", "SQL", "Machine Learning"]',
        '["TensorFlow", "PyTorch"]',
        '[{"issue": "Missing TensorFlow/PyTorch explicitly", "severity": "HIGH"}, {"issue": "GPA not listed on resume", "severity": "MEDIUM"}]',
        '[{"suggestion": "Add TensorFlow and PyTorch to skills section", "priority": "HIGH"}, {"suggestion": "Emphasize PhD credentials more prominently", "priority": "HIGH"}, {"suggestion": "Add research publications to resume", "priority": "MEDIUM"}]');

-- ==============================================================================
-- Optimization Versions
-- ==============================================================================
INSERT INTO optimization_versions (id, analysis_id, user_id, version_number, optimized_content, change_log, new_overall_score, score_improvement, is_active)
VALUES
    ('e47ac10b-58cc-4372-a567-0e02b2c3d479', 'd47ac10b-58cc-4372-a567-0e02b2c3d479', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 1,
        'Bob Smith | Senior Software Engineer | bob@example.com | (555) 123-4567 | AWS Certified Solutions Architect\n\nCORE COMPETENCIES:\nBackend Development | Microservices Architecture | Java & Spring Boot | Kubernetes | Docker | PostgreSQL | REST APIs | Cloud Infrastructure (AWS) | Event-Driven Architecture | Distributed Systems\n\nEXPERIENCE:\nSenior Software Engineer at TechCorp (2021-Present)\n- Architected and implemented distributed microservices platform using Java Spring Boot and Kubernetes, reducing deployment time by 40%\n- Led cross-functional team of 5 engineers through complete system redesign, improving API response times by 45%\n- Designed and implemented event-driven architecture processing 100K+ transactions daily using Docker and AWS\n- Established CI/CD pipelines reducing production incidents by 60%\n\nSoftware Engineer at StartupXYZ (2018-2021)\n- Built full-stack applications using Java Spring Boot backend and React frontend\n- Implemented Kubernetes orchestration for containerized microservices\n- Developed PostgreSQL schemas supporting 10M+ daily queries\n\nEDUCATION:\nBS Computer Science - State University (2018)\n\nCERTIFICATIONS:\n- AWS Solutions Architect Associate\n- Certified Kubernetes Administrator\n',
        '[{"change": "Added AWS keyword and certification details", "section": "SKILLS"}, {"change": "Quantified all achievements with metrics", "section": "EXPERIENCE"}, {"change": "Added Core Competencies section for ATS", "section": "HEADER"}]',
        95.20, 7.70, true);

-- ==============================================================================
-- Favorites
-- ==============================================================================
INSERT INTO favorites (id, user_id, analysis_id, label, is_favorited)
VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d482', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'd47ac10b-58cc-4372-a567-0e02b2c3d479', 'Strong Match - TechCorp', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d483', 'a47ac10b-58cc-4372-a567-0e02b2c3d481', 'd47ac10b-58cc-4372-a567-0e02b2c3d480', 'Needs Optimization - InnovationLabs', true);

-- ==============================================================================
-- Notifications
-- ==============================================================================
INSERT INTO notifications (id, user_id, notification_type, title, message, is_read)
VALUES
    ('g47ac10b-58cc-4372-a567-0e02b2c3d479', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'ANALYSIS_COMPLETE', 'Resume Analysis Complete', 'Your resume analysis for TechCorp position is ready', false),
    ('g47ac10b-58cc-4372-a567-0e02b2c3d480', 'a47ac10b-58cc-4372-a567-0e02b2c3d481', 'OPTIMIZATION_READY', 'Optimization Available', 'AI-powered resume optimization is ready for your review', false);

-- ==============================================================================
-- Audit Logs (Sample Activity)
-- ==============================================================================
INSERT INTO audit_logs (id, user_id, action, entity_type, entity_id, details, ip_address, http_method, http_status_code)
VALUES
    ('h47ac10b-58cc-4372-a567-0e02b2c3d479', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'UPLOAD', 'RESUME', 'b47ac10b-58cc-4372-a567-0e02b2c3d479', '{"filename": "Bob_Smith_Resume.pdf", "size": 125000}', '192.168.1.100'::inet, 'POST', 201),
    ('h47ac10b-58cc-4372-a567-0e02b2c3d480', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'ANALYZE', 'ANALYSIS_RESULT', 'd47ac10b-58cc-4372-a567-0e02b2c3d479', '{"score": 87.5}', '192.168.1.100'::inet, 'POST', 201),
    ('h47ac10b-58cc-4372-a567-0e02b2c3d481', 'a47ac10b-58cc-4372-a567-0e02b2c3d480', 'OPTIMIZE', 'OPTIMIZATION_VERSION', 'e47ac10b-58cc-4372-a567-0e02b2c3d479', '{"new_score": 95.2, "improvements": 7.7}', '192.168.1.100'::inet, 'POST', 201);

-- Re-enable constraints
ALTER TABLE audit_logs ENABLE TRIGGER ALL;
ALTER TABLE notifications ENABLE TRIGGER ALL;

-- ==============================================================================
-- Sample Comments
-- ==============================================================================
-- Note: These are placeholder comments for documentation
-- SAMPLE TEST QUERIES:
-- SELECT COUNT(*) FROM users WHERE is_active = true;
-- SELECT AVG(overall_score) FROM analysis_results WHERE created_at > NOW() - INTERVAL '7 days';
-- SELECT * FROM audit_logs WHERE action = 'ANALYZE' ORDER BY created_at DESC LIMIT 10;
