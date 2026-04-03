-- ==============================================================================
-- HireMind AI - Database Schema
-- PostgreSQL 15+
-- ==============================================================================

-- Enable pgvector extension for AI/ML vector operations
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "vector";

-- ==============================================================================
-- Users Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'RECRUITER', 'ADMIN', 'ENTERPRISE_ADMIN')),
    organization_id UUID,
    is_active BOOLEAN DEFAULT true,
    email_verified BOOLEAN DEFAULT false,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_organization_id ON users(organization_id);
CREATE INDEX idx_users_is_active ON users(is_active);

-- ==============================================================================
-- Resumes Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS resumes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    original_filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(512) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    mime_type VARCHAR(100),
    parsed_content TEXT,
    raw_text TEXT,
    skills_extracted JSONB DEFAULT '[]',
    experiences JSONB DEFAULT '[]',
    education JSONB DEFAULT '[]',
    contact_info JSONB DEFAULT '{}',
    certifications JSONB DEFAULT '[]',
    languages JSONB DEFAULT '[]',
    summary TEXT,
    embedding vector(1536),
    parsing_status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK (parsing_status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED')),
    parsing_error_message TEXT,
    is_deleted BOOLEAN DEFAULT false,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_resumes_user_id ON resumes(user_id);
CREATE INDEX idx_resumes_parsing_status ON resumes(parsing_status);
CREATE INDEX idx_resumes_created_at ON resumes(created_at);
CREATE INDEX idx_resumes_is_deleted ON resumes(is_deleted);
CREATE INDEX idx_resumes_embedding ON resumes USING ivfflat (embedding vector_cosine_ops);

-- ==============================================================================
-- Job Descriptions Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS job_descriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    job_url VARCHAR(2048),
    raw_text TEXT NOT NULL,
    parsed_skills JSONB DEFAULT '[]',
    parsed_requirements JSONB DEFAULT '{}',
    parsed_qualifications JSONB DEFAULT '{}',
    preferred_qualifications JSONB DEFAULT '{}',
    responsibilities JSONB DEFAULT '[]',
    salary_range JSONB DEFAULT '{}',
    employment_type VARCHAR(50),
    experience_level VARCHAR(50),
    industry VARCHAR(100),
    department VARCHAR(100),
    embedding vector(1536),
    parsed_successfully BOOLEAN DEFAULT false,
    parsing_error_message TEXT,
    is_deleted BOOLEAN DEFAULT false,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_job_descriptions_user_id ON job_descriptions(user_id);
CREATE INDEX idx_job_descriptions_company ON job_descriptions(company);
CREATE INDEX idx_job_descriptions_created_at ON job_descriptions(created_at);
CREATE INDEX idx_job_descriptions_is_deleted ON job_descriptions(is_deleted);
CREATE INDEX idx_job_descriptions_embedding ON job_descriptions USING ivfflat (embedding vector_cosine_ops);

-- ==============================================================================
-- Analysis Results Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS analysis_results (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    resume_id UUID NOT NULL REFERENCES resumes(id) ON DELETE CASCADE,
    job_description_id UUID NOT NULL REFERENCES job_descriptions(id) ON DELETE CASCADE,
    overall_score NUMERIC(5, 2) NOT NULL CHECK (overall_score >= 0 AND overall_score <= 100),
    score_breakdown JSONB NOT NULL DEFAULT '{}',
    matched_skills JSONB DEFAULT '[]',
    missing_skills JSONB DEFAULT '[]',
    partial_match_skills JSONB DEFAULT '[]',
    ats_issues JSONB DEFAULT '[]',
    ats_compatibility_score NUMERIC(5, 2),
    readability_score NUMERIC(5, 2),
    keyword_optimization_score NUMERIC(5, 2),
    formatting_issues JSONB DEFAULT '[]',
    suggestions JSONB DEFAULT '[]',
    semantic_similarity NUMERIC(5, 4),
    analysis_metadata JSONB DEFAULT '{}',
    analyzed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_analysis_results_user_id ON analysis_results(user_id);
CREATE INDEX idx_analysis_results_resume_id ON analysis_results(resume_id);
CREATE INDEX idx_analysis_results_job_description_id ON analysis_results(job_description_id);
CREATE INDEX idx_analysis_results_overall_score ON analysis_results(overall_score);
CREATE INDEX idx_analysis_results_created_at ON analysis_results(created_at);

-- ==============================================================================
-- Optimization Versions Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS optimization_versions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    analysis_id UUID NOT NULL REFERENCES analysis_results(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    version_number INTEGER NOT NULL,
    optimized_content TEXT NOT NULL,
    change_log JSONB NOT NULL DEFAULT '[]',
    optimization_strategy JSONB NOT NULL DEFAULT '{}',
    new_overall_score NUMERIC(5, 2),
    score_improvement NUMERIC(5, 2),
    ats_score_improvement NUMERIC(5, 2),
    blocked_claims JSONB DEFAULT '[]',
    modifications_summary TEXT,
    is_active BOOLEAN DEFAULT false,
    exported_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_optimization_versions_analysis_id ON optimization_versions(analysis_id);
CREATE INDEX idx_optimization_versions_user_id ON optimization_versions(user_id);
CREATE INDEX idx_optimization_versions_version_number ON optimization_versions(version_number);
CREATE INDEX idx_optimization_versions_is_active ON optimization_versions(is_active);

-- ==============================================================================
-- Audit Logs Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,
    details JSONB DEFAULT '{}',
    ip_address INET,
    user_agent VARCHAR(512),
    resource_path VARCHAR(512),
    http_method VARCHAR(10),
    http_status_code INTEGER,
    error_message TEXT,
    response_time_ms INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity_type ON audit_logs(entity_type);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_ip_address ON audit_logs(ip_address);

-- ==============================================================================
-- Favorites/Bookmarks Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS favorites (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    analysis_id UUID NOT NULL REFERENCES analysis_results(id) ON DELETE CASCADE,
    label VARCHAR(255),
    is_favorited BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, analysis_id)
);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_analysis_id ON favorites(analysis_id);
CREATE INDEX idx_favorites_is_favorited ON favorites(is_favorited);

-- ==============================================================================
-- Notifications Table
-- ==============================================================================
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    notification_type VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    related_entity_type VARCHAR(100),
    related_entity_id UUID,
    is_read BOOLEAN DEFAULT false,
    read_at TIMESTAMP,
    action_url VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- ==============================================================================
-- Organizations Table (for Enterprise)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS organizations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    website VARCHAR(512),
    industry VARCHAR(100),
    employee_count INTEGER,
    subscription_tier VARCHAR(50) NOT NULL DEFAULT 'FREE' CHECK (subscription_tier IN ('FREE', 'STARTER', 'PROFESSIONAL', 'ENTERPRISE')),
    max_users INTEGER DEFAULT 1,
    max_resumes INTEGER DEFAULT 10,
    max_job_descriptions INTEGER DEFAULT 10,
    is_active BOOLEAN DEFAULT true,
    billing_email VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_organizations_name ON organizations(name);
CREATE INDEX idx_organizations_subscription_tier ON organizations(subscription_tier);

-- ==============================================================================
-- Update Trigger for updated_at Columns
-- ==============================================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_resumes_updated_at BEFORE UPDATE ON resumes
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_job_descriptions_updated_at BEFORE UPDATE ON job_descriptions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_analysis_results_updated_at BEFORE UPDATE ON analysis_results
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_optimization_versions_updated_at BEFORE UPDATE ON optimization_versions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ==============================================================================
-- Constraints & Relationships
-- ==============================================================================
ALTER TABLE users ADD CONSTRAINT fk_users_organization_id
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE SET NULL;

-- Grant permissions for application user
GRANT CONNECT ON DATABASE hiremind_db TO hiremind_user;
GRANT USAGE ON SCHEMA public TO hiremind_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO hiremind_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO hiremind_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO hiremind_user;
