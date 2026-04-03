# HireMind AI - System Architecture

## Overview

HireMind AI is an enterprise-grade, AI-powered resume intelligence and ATS optimization platform. The system combines advanced natural language processing, machine learning, and vector similarity algorithms to analyze resumes against job descriptions, identify gaps, and provide intelligent optimization recommendations.

The architecture follows a scalable microservices-ready design with clear separation of concerns, supporting horizontal scaling and high availability.

## High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                                    │
├─────────────────────────────────────────────────────────────────────────┤
│  Web Browser (React SPA)  │  Mobile Web  │  API Clients                │
└────────────┬──────────────┴──────────┬───┴──────────────────────────────┘
             │                        │
             └────────────┬───────────┘
                          │ HTTPS/REST
┌─────────────────────────▼───────────────────────────────────────────────┐
│                    API GATEWAY / REVERSE PROXY                          │
│                         (Nginx / Load Balancer)                         │
└─────────────────────────┬───────────────────────────────────────────────┘
                          │
        ┌─────────────────┴────────────────┐
        │                                  │
┌───────▼──────────────────────┐   ┌──────▼──────────────────────┐
│  BACKEND SERVICE LAYER       │   │  FRONTEND LAYER             │
│  (Java Spring Boot)          │   │  (React 18 + TypeScript)    │
│                              │   │                             │
│  ┌──────────────────────┐    │   │  ┌──────────────────────┐  │
│  │ Authentication       │    │   │  │ Dashboard            │  │
│  │ - JWT Token Service  │    │   │  │ - Resume Upload      │  │
│  │ - OAuth2 Integration │    │   │  │ - Job Description    │  │
│  └──────────────────────┘    │   │  │ - Analysis Results   │  │
│                              │   │  │ - Optimization View  │  │
│  ┌──────────────────────┐    │   │  └──────────────────────┘  │
│  │ Resume Service       │    │   │                             │
│  │ - File Upload        │    │   │  ┌──────────────────────┐  │
│  │ - PDF Parsing        │    │   │  │ Analytics            │  │
│  │ - Skill Extraction   │    │   │  │ - Score Trends       │  │
│  │ - Embedding Gen      │    │   │  │ - Performance Stats  │  │
│  └──────────────────────┘    │   │  └──────────────────────┘  │
│                              │   │                             │
│  ┌──────────────────────┐    │   └─────────────────────────────┘
│  │ Analysis Engine      │    │
│  │ - Score Calculator   │    │
│  │ - Skill Matcher      │    │
│  │ - ATS Checker        │    │
│  │ - Similarity Scoring │    │
│  └──────────────────────┘    │
│                              │
│  ┌──────────────────────┐    │
│  │ Optimization Service │    │
│  │ - Content Generator  │    │
│  │ - LLM Integration    │    │
│  │ - Version Management │    │
│  └──────────────────────┘    │
│                              │
│  ┌──────────────────────┐    │
│  │ Job Description Svc  │    │
│  │ - JD Parsing         │    │
│  │ - Requirement Extraction│ │
│  │ - Skill Mapping      │    │
│  └──────────────────────┘    │
│                              │
│  ┌──────────────────────┐    │
│  │ Audit & Security     │    │
│  │ - Role-Based Access  │    │
│  │ - Audit Logging      │    │
│  │ - Rate Limiting      │    │
│  └──────────────────────┘    │
└───────┬──────────────────────┘
        │
┌───────▼──────────────────────────────────────────────────────────────────┐
│                      DATA LAYER                                          │
├──────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌────────────────────────┐              ┌──────────────────────────┐  │
│  │ PostgreSQL Database    │              │ Vector Store (pgvector)  │  │
│  │ (Primary Data Store)   │              │ (Embeddings Index)       │  │
│  │                        │              │                          │  │
│  │ - Users & Auth         │              │ - Resume Embeddings      │  │
│  │ - Resumes              │              │ - JD Embeddings         │  │
│  │ - Job Descriptions     │              │ - Similarity Search     │  │
│  │ - Analysis Results     │              │                          │  │
│  │ - Optimizations        │              └──────────────────────────┘  │
│  │ - Audit Logs           │              │                          │
│  │ - Favorites            │              │                          │
│  │ - Organizations        │              │                          │
│  └────────────────────────┘              └──────────────────────────┘  │
│                                                                          │
│  ┌────────────────────────────────────────────────────────────────┐    │
│  │ File Storage                                                    │    │
│  │ - Resume Files (PDF, DOCX)                                     │    │
│  │ - Optimized Resume Versions                                    │    │
│  │ - Export Formats                                               │    │
│  └────────────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────────────┘
        │
        └─────────────────┬────────────────────────────┐
                          │                            │
        ┌─────────────────▼──────┐      ┌──────────────▼─────────┐
        │   AI/ML INTEGRATION    │      │  EXTERNAL SERVICES     │
        │                        │      │                        │
        │ - OpenAI (GPT-4)       │      │ - Email Service        │
        │ - Anthropic (Claude)   │      │ - Analytics (optional) │
        │ - Embedding Models     │      │ - Monitoring (optional)│
        │ - NLP Pipelines        │      │ - Error Tracking       │
        └────────────────────────┘      └────────────────────────┘
```

## Core Components

### 1. Frontend Layer (React 18 + TypeScript)

**Technology Stack:**
- React 18 for UI rendering
- TypeScript for type safety
- Redux or Context API for state management
- Tailwind CSS for styling
- Recharts for data visualization
- Axios for API communication

**Key Pages:**
- **Dashboard:** Main landing page with quick stats
- **Resume Manager:** Upload, view, and manage resumes
- **Job Description Manager:** Input or paste job descriptions
- **Analysis Results:** Display matching scores and breakdown
- **Optimization View:** Show before/after comparisons
- **Settings:** User preferences and account management
- **Analytics:** Historical data and trends

**Features:**
- Real-time feedback during analysis
- Visual score breakdowns
- Side-by-side resume comparison
- Export functionality (PDF, DOCX)
- Responsive design for mobile

### 2. Backend Service Layer (Java 17 + Spring Boot 3.2)

#### 2.1 Authentication & Authorization Service
- JWT token generation and validation
- OAuth2 integration (Google, GitHub)
- Role-Based Access Control (RBAC)
- Session management
- Multi-factor authentication (MFA) ready

**Roles:**
- `USER`: Individual job seeker
- `RECRUITER`: Organization recruiter
- `ADMIN`: System administrator
- `ENTERPRISE_ADMIN`: Enterprise administrator

#### 2.2 Resume Service
- **File Upload Management:** Handle PDF, DOCX, DOC, TXT
- **PDF Parsing:** Extract text, metadata, structure
- **Content Processing:**
  - Extract work experience
  - Extract education history
  - Extract skills and certifications
  - Parse contact information
  - Generate text embeddings
- **Storage:** File system or cloud storage (S3-ready)
- **Versioning:** Track resume iterations

#### 2.3 Job Description Service
- Parse job descriptions from text
- Extract required and preferred skills
- Identify job requirements and qualifications
- Parse employment type, experience level, location
- Generate embeddings for semantic search
- Store and organize multiple job descriptions

#### 2.4 Analysis Engine
**6-Dimension Scoring Algorithm:**

1. **Required Skills Match (35%)**
   - Match resume skills against job requirements
   - Exact keyword matching + fuzzy matching
   - Penalize for significant gaps

2. **Preferred Skills Match (15%)**
   - Bonus points for nice-to-have skills
   - Weighted by relevance

3. **Semantic Similarity (15%)**
   - Use embedding vectors for deep semantic understanding
   - Compare experience descriptions with job requirements
   - Cosine similarity scoring

4. **Domain Alignment (10%)**
   - Industry relevance
   - Role history alignment
   - Career progression analysis

5. **ATS Keyword Optimization (15%)**
   - Analyze resume formatting
   - Check for ATS-blocking elements
   - Keyword density analysis
   - Verify parseable structure

6. **Resume Formatting (10%)**
   - Visual hierarchy
   - Consistent formatting
   - Readability score
   - No images/graphics that break ATS

**Output:**
- Overall compatibility score (0-100)
- Detailed breakdown by dimension
- Matched skills list
- Missing skills list
- ATS issues identified
- Personalized suggestions

#### 2.5 Optimization Service
- AI-powered content generation using LLMs
- Keyword insertion strategies
- Achievement quantification
- ATS-friendly formatting recommendations
- Version control for optimized resumes
- Change tracking and rollback capability
- Score improvement estimation

#### 2.6 Data Access Layer (JPA/Hibernate)
- Repository pattern implementation
- Custom queries for analysis
- Transaction management
- Connection pooling

### 3. Data Layer

#### 3.1 PostgreSQL 15 Database

**Core Tables:**
- `users` - User accounts and authentication
- `organizations` - Multi-tenant support
- `resumes` - Resume documents and parsed content
- `job_descriptions` - Job postings and parsed requirements
- `analysis_results` - Match analysis and scores
- `optimization_versions` - Resume optimization history
- `audit_logs` - Complete activity trail
- `favorites` - User bookmarks
- `notifications` - User alerts

**Indexes:**
- User ID indexes for query acceleration
- Timestamp indexes for range queries
- Status indexes for filtering
- Full-text search indexes (optional)

#### 3.2 Vector Store (pgvector Extension)
- Store embedding vectors (1536-dimensional OpenAI embeddings)
- Enable semantic similarity search
- IVFFlat indexing for performance
- Used for:
  - Resume-to-JD similarity matching
  - Content-based recommendations
  - Skill fuzzy matching

#### 3.3 File Storage
- Local file system for development
- S3-compatible storage for production
- Organized by user ID and resume ID
- Secure access controls

### 4. External Integrations

#### 4.1 LLM Services
- **OpenAI (GPT-4):** Content generation, analysis
- **Anthropic (Claude):** Alternative LLM provider
- Circuit breaker pattern for fault tolerance
- Response caching for common queries

#### 4.2 Email Service (Optional)
- Resume optimization notifications
- Analysis alerts
- User invitations
- Support integration

#### 4.3 Monitoring & Observability
- Logging: ELK Stack or Cloud Logging
- Metrics: Prometheus + Grafana (optional)
- Error Tracking: Sentry or similar
- Performance monitoring: APM tools

## API Endpoints Overview

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/verify-email` - Email verification

### Resume Management
- `POST /api/resumes/upload` - Upload resume file
- `GET /api/resumes` - List user resumes
- `GET /api/resumes/{id}` - Get resume details
- `DELETE /api/resumes/{id}` - Delete resume
- `GET /api/resumes/{id}/content` - Get parsed content

### Job Descriptions
- `POST /api/job-descriptions` - Create job description
- `GET /api/job-descriptions` - List job descriptions
- `GET /api/job-descriptions/{id}` - Get JD details
- `PUT /api/job-descriptions/{id}` - Update JD
- `DELETE /api/job-descriptions/{id}` - Delete JD

### Analysis
- `POST /api/analysis` - Trigger analysis
- `GET /api/analysis/{id}` - Get analysis results
- `GET /api/analysis/resume/{resumeId}/jobs/{jobId}` - Get specific analysis
- `GET /api/analysis/history` - Get analysis history

### Optimization
- `POST /api/optimization/{analysisId}` - Generate optimization
- `GET /api/optimization/versions/{analysisId}` - Get optimization versions
- `GET /api/optimization/versions/{versionId}` - Get specific version
- `POST /api/optimization/export/{versionId}` - Export optimized resume

### Admin/Audit
- `GET /api/audit-logs` - View audit logs (admin only)
- `GET /api/analytics/dashboard` - Dashboard metrics (admin only)

## Deployment Architecture

### Development
```
docker-compose up
- Local PostgreSQL
- PgAdmin for DB management
- Backend on localhost:8080
- Frontend on localhost:3000
```

### Production
```
- Docker containers on Kubernetes cluster
- PostgreSQL managed database (RDS/Cloud SQL)
- Redis for caching (optional)
- CDN for static assets
- Load balancer for backend services
- Separate ingress controller
```

## Security Architecture

### Authentication & Authorization
- JWT tokens with 24-hour expiration
- Refresh token rotation
- Role-based access control (RBAC)
- Fine-grained permission model

### Data Protection
- Passwords hashed with bcrypt (min 12 rounds)
- TLS/SSL for all transit
- Encrypted storage of sensitive fields
- PII masking in logs

### Audit & Compliance
- Complete audit trail of all actions
- User action logging with IP addresses
- Compliance-ready logging format
- GDPR data deletion support

## Performance Considerations

### Caching Strategy
- Redis for session management
- Database query caching
- API response caching (30-60 seconds)
- Client-side caching with ETags

### Optimization
- Database connection pooling (HikariCP)
- Lazy loading of relationships
- Batch processing for bulk operations
- Async processing for long-running tasks (background jobs)

### Scalability
- Horizontal scaling of backend instances
- Read replicas for database
- Separate read/write databases (optional)
- Message queue for async processing (RabbitMQ/Kafka optional)

## Monitoring & Observability

### Metrics
- API request rates and latencies
- Database query performance
- LLM API call metrics
- Storage utilization
- User activity metrics

### Logging
- Structured JSON logging
- Correlation IDs for request tracing
- Separate logs for different services
- Log aggregation and analysis

### Alerts
- API error rate thresholds
- Database connection pool warnings
- LLM service availability
- Disk space alerts
- Authentication failure monitoring

## Future Enhancements

1. **Real-time Collaboration:** Multiple users analyzing same resume
2. **Advanced Analytics:** Predictive match scoring using historical data
3. **Batch Processing:** Process thousands of resumes against job descriptions
4. **Video Interview Integration:** Analyze interview transcripts
5. **LinkedIn Integration:** Auto-import resume from LinkedIn
6. **Multi-language Support:** Resume optimization in multiple languages
7. **Industry-Specific Models:** Custom scoring for different industries
8. **Resume Template Library:** Pre-built ATS-optimized templates
9. **Machine Learning Model Improvements:** Custom trained models over time
10. **Mobile Applications:** Native iOS/Android apps

## Dependencies & Technology Choices

### Backend Dependencies
- Spring Boot 3.2+
- Spring Security 6.0+
- Spring Data JPA
- Hibernate ORM
- PostgreSQL JDBC Driver
- JWT (jjwt)
- Jackson (JSON processing)
- Apache PDFBox (PDF parsing)
- Apache POI (DOCX parsing)
- OpenAI Java Client
- OpenSearch/Elasticsearch (optional)

### Frontend Dependencies
- React 18
- TypeScript 5
- React Router v6
- Axios for HTTP
- Redux or Zustand for state
- Tailwind CSS
- Recharts for charting
- React Query for data fetching
- Jest & React Testing Library

## Conclusion

HireMind AI's architecture is designed for enterprise-scale operations with security, performance, and user experience as primary concerns. The modular design allows for independent scaling of components and easy integration of new features without disrupting existing functionality.
