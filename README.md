# HireMind AI

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?style=flat&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB?style=flat&logo=react)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-3178C6?style=flat&logo=typescript)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=flat&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## Overview

**HireMind AI** is an enterprise-grade, AI-powered resume intelligence and ATS optimization platform designed to bridge the gap between job seekers and employer requirements. Our platform combines advanced natural language processing, machine learning algorithms, and vector similarity analysis to provide actionable insights for resume optimization.

### What It Does

HireMind AI automatically analyzes your resume against job descriptions using a proprietary 6-dimension scoring algorithm that evaluates:
- Skills alignment with job requirements
- Semantic understanding of experience and role fit
- ATS (Applicant Tracking System) compatibility
- Resume formatting and readability
- Industry-specific domain knowledge

### Why It Matters

- **Increase Interview Callbacks:** Get resumes past ATS systems and into recruiter hands
- **Save Time:** Skip weeks of resume tweaking with AI-powered optimization suggestions
- **Data-Driven Decisions:** Understand exactly what's missing and how to improve
- **Maintain Authenticity:** All suggestions respect your actual experience and skills
- **Multiple Versions:** Generate and compare optimized versions without permanent changes

---

## Architecture

HireMind AI follows a modern, scalable microservices-ready architecture with clear separation of concerns.

### System Architecture Overview

```
┌──────────────────────────────────────────────────────────────────┐
│                    Frontend (React 18 + TypeScript)              │
│        Dashboard | Resume Upload | Analysis | Optimization       │
└────────────┬─────────────────────────────────────────────────────┘
             │ REST API (HTTPS)
┌────────────▼─────────────────────────────────────────────────────┐
│              Backend (Java 17 Spring Boot 3.2)                   │
│  ┌──────────────────────────────────────────────────────────┐    │
│  │ Services: Auth | Resume | JD | Analysis | Optimization  │    │
│  │ Security: JWT | RBAC | Audit Logging                    │    │
│  └──────────────────────────────────────────────────────────┘    │
└────────────┬─────────────────────────────────────────────────────┘
             │ JDBC / pgvector
┌────────────▼─────────────────────────────────────────────────────┐
│   PostgreSQL 15 (Users | Resumes | JD | Analysis | Audit)       │
│   Vector Store (pgvector - Semantic Embeddings)                  │
│   File Storage (PDF/DOCX Resume Uploads)                         │
└────────────┬─────────────────────────────────────────────────────┘
             │
┌────────────▼─────────────────────────────────────────────────────┐
│  External AI Services: OpenAI (GPT-4) | Anthropic (Claude)       │
│  Embedding Models | NLP Pipelines | Content Generation           │
└──────────────────────────────────────────────────────────────────┘
```

For detailed architecture documentation, see [docs/architecture.md](docs/architecture.md).

---

## Key Features

### AI-Powered Resume Analysis
- Automated parsing of resume structure and content
- Extraction of skills, experience, education, and certifications
- Semantic understanding of career progression and domain expertise
- Support for PDF, DOCX, DOC, and TXT formats

### Explainable Match Scoring (6-Dimension Algorithm)
Transparent scoring breakdown helping you understand exactly why you match (or don't):

| Dimension | Weight | What It Measures |
|-----------|--------|------------------|
| Required Skills Match | 35% | Keywords and skills listed in job requirements |
| Preferred Skills Match | 15% | Nice-to-have skills that differentiate candidates |
| Semantic Similarity | 15% | Deep understanding of your experience relevance |
| Domain Alignment | 10% | Industry, role type, and career path fit |
| ATS Keyword Optimization | 15% | Proper formatting and keyword distribution for ATS systems |
| Resume Formatting | 10% | Structure, readability, and visual hierarchy |

### ATS Compatibility Detection
- Identifies formatting issues that break ATS parsing
- Detects missing critical keywords
- Validates proper use of sections and bullet points
- Provides specific remediation steps

### Intelligent Resume Optimization
- AI-powered content generation using GPT-4 or Claude
- Keyword insertion strategies that maintain authenticity
- Achievement quantification with metrics
- Industry-specific language recommendations
- Multiple optimization versions for A/B testing

### Before/After Comparison with Score Improvement
- Side-by-side resume comparison
- Visual score progression tracking
- Specific changes highlighted and explained
- Estimated score improvement

### ATS-Friendly Resume Export
- Download optimized resumes in multiple formats
- PDF export with ATS-safe formatting
- DOCX export for easy further editing
- Track which versions were exported

### Role-Based Access Control (RBAC)
- **Job Seeker:** Personal resume analysis and optimization
- **Recruiter:** Analyze candidate resumes, bulk matching
- **Administrator:** Platform management, user management
- **Enterprise Admin:** Organization settings, team management

### Comprehensive Audit Trail
- Complete activity logging for compliance
- User action tracking with timestamps and IP addresses
- Entity change history
- Export for regulatory requirements (SOC2, GDPR)

---

## Tech Stack

### Backend
- **Language:** Java 17 with modern features
- **Framework:** Spring Boot 3.2 with reactive capabilities
- **Security:** Spring Security 6.0, JWT tokens, OAuth2
- **Database:** JPA/Hibernate ORM for data persistence
- **API:** RESTful API with OpenAPI/Swagger documentation
- **PDF Processing:** Apache PDFBox for resume extraction
- **Word Processing:** Apache POI for DOCX/DOC support
- **Testing:** JUnit 5, Mockito, TestContainers

### Frontend
- **Framework:** React 18 with Hooks
- **Language:** TypeScript 5 for type safety
- **State Management:** Context API or Redux
- **Styling:** Tailwind CSS for modern, responsive UI
- **HTTP Client:** Axios with interceptors
- **Data Visualization:** Recharts for score breakdowns
- **Testing:** Jest, React Testing Library

### Database
- **DBMS:** PostgreSQL 15 with pgvector extension
- **Caching:** Redis (optional) for sessions and caching
- **Search:** Full-text search capabilities
- **Analytics:** Time-series data for trend analysis

### DevOps & Deployment
- **Containerization:** Docker with multi-stage builds
- **Orchestration:** Docker Compose for local dev, Kubernetes for prod
- **CI/CD:** GitHub Actions (readily adaptable to GitLab, Jenkins)
- **Monitoring:** Prometheus, Grafana (optional)
- **Logging:** ELK Stack or Cloud Logging integration

### AI & Machine Learning
- **LLM Provider:** OpenAI (GPT-4) or Anthropic (Claude)
- **Embeddings:** OpenAI's text-embedding-3-small (1536 dimensions)
- **Vector Search:** pgvector with IVFFlat indexing
- **NLP:** Sentence transformers for semantic analysis

---

## Scoring Algorithm Details

### 6-Dimension Weighted Score Calculation

```
Overall Score = (0.35 × Required Skills Match)
              + (0.15 × Preferred Skills Match)
              + (0.15 × Semantic Similarity)
              + (0.10 × Domain Alignment)
              + (0.15 × ATS Optimization)
              + (0.10 × Formatting)
```

### Score Interpretation
- **90-100:** Excellent match, significant advantage in hiring process
- **80-89:** Strong match, well-positioned in candidate pool
- **70-79:** Good match, competitive but room for optimization
- **60-69:** Fair match, meaningful gaps to address
- **50-59:** Weak match, significant skill/experience gaps
- **0-49:** Poor match, not well-suited for this position

### Matched Skills Example
```json
{
  "matched": ["Java", "Spring Boot", "Kubernetes", "Docker"],
  "missing": ["AWS"],
  "partial_matches": ["Microservices"],
  "exact_keyword_matches": 4,
  "fuzzy_matches": 1
}
```

---

## Getting Started

### Prerequisites

- **Docker & Docker Compose** (recommended for quickstart)
  ```bash
  docker --version  # v20.10+
  docker-compose --version  # v2.0+
  ```

- **Java 17+** (for manual backend setup)
  ```bash
  java -version  # openjdk 17.0+
  ```

- **Node.js 18+** (for manual frontend setup)
  ```bash
  node --version  # v18.0+
  npm --version  # v9.0+
  ```

- **PostgreSQL 15+** (if not using Docker)
  ```bash
  psql --version  # PostgreSQL 15+
  ```

### Quickstart with Docker

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/hiremind-ai.git
   cd hiremind-ai
   ```

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start all services**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - PgAdmin: http://localhost:5050 (admin@hiremind.local / admin)

5. **Check service health**
   ```bash
   docker-compose ps
   curl http://localhost:8080/api/health
   ```

### Manual Setup

#### Backend Setup
```bash
cd backend

# Build the project
mvn clean package

# Run the application
java -jar target/hiremind-api-1.0.0.jar

# The backend will be available at http://localhost:8080
```

#### Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start

# The frontend will be available at http://localhost:3000
```

#### Database Setup
```bash
# Create database and user
createdb -U postgres hiremind_db
psql -U postgres -d hiremind_db -f docs/database/schema.sql

# Seed sample data (optional)
psql -U postgres -d hiremind_db -f docs/database/seed.sql
```

### Environment Variables

Key environment variables to configure:

```env
# Database
DB_NAME=hiremind_db
DB_USER=hiremind_user
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your-secret-key-minimum-32-characters
JWT_EXPIRATION=86400000  # 24 hours in milliseconds

# AI Services
OPENAI_API_KEY=sk_test_your_key_here
ANTHROPIC_API_KEY=sk_ant_your_key_here

# Frontend
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENV=production
```

See [.env.example](.env.example) for complete list.

---

## API Endpoints

### Authentication
```
POST   /api/auth/register              Create new account
POST   /api/auth/login                 User login (returns JWT)
POST   /api/auth/logout                User logout
POST   /api/auth/refresh               Refresh JWT token
POST   /api/auth/verify-email          Verify email address
```

### Resumes
```
POST   /api/resumes/upload             Upload resume file (PDF, DOCX)
GET    /api/resumes                    List all user resumes
GET    /api/resumes/{id}               Get resume details
GET    /api/resumes/{id}/content       Get parsed resume content
DELETE /api/resumes/{id}               Delete resume
```

### Job Descriptions
```
POST   /api/job-descriptions           Create job description
GET    /api/job-descriptions           List all job descriptions
GET    /api/job-descriptions/{id}      Get job description details
PUT    /api/job-descriptions/{id}      Update job description
DELETE /api/job-descriptions/{id}      Delete job description
```

### Analysis
```
POST   /api/analysis                   Trigger analysis (resume vs JD)
GET    /api/analysis/{id}              Get analysis results
GET    /api/analysis/history           Get analysis history
DELETE /api/analysis/{id}              Delete analysis
```

### Optimization
```
POST   /api/optimization/{analysisId}           Generate optimized resume
GET    /api/optimization/versions/{analysisId}  Get all optimization versions
GET    /api/optimization/versions/{versionId}   Get specific optimization
POST   /api/optimization/export/{versionId}     Export optimized resume
```

### Admin/Analytics
```
GET    /api/admin/analytics            Dashboard metrics
GET    /api/admin/audit-logs           View audit trail
GET    /api/admin/users                Manage users
```

See [API Documentation](docs/api.md) for request/response examples.

---

## Project Structure

```
hiremind-ai/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hiremind/
│   │   │   │   ├── config/              # Spring configuration
│   │   │   │   ├── controller/          # REST controllers
│   │   │   │   ├── service/             # Business logic
│   │   │   │   ├── repository/          # Data access
│   │   │   │   ├── model/               # Entity classes
│   │   │   │   ├── dto/                 # Transfer objects
│   │   │   │   ├── security/            # Auth & JWT
│   │   │   │   ├── util/                # Utilities
│   │   │   │   └── HireMindApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml      # Configuration
│   │   │       └── db/migration/        # Database migrations
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/              # React components
│   │   │   ├── Dashboard/
│   │   │   ├── ResumeUpload/
│   │   │   ├── Analysis/
│   │   │   └── Optimization/
│   │   ├── pages/                   # Page components
│   │   ├── services/                # API clients
│   │   ├── store/                   # State management
│   │   ├── types/                   # TypeScript interfaces
│   │   ├── App.tsx
│   │   └── index.tsx
│   ├── public/
│   ├── Dockerfile
│   ├── package.json
│   ├── tsconfig.json
│   └── tailwind.config.js
│
├── docs/
│   ├── database/
│   │   ├── schema.sql               # Database schema
│   │   └── seed.sql                 # Sample data
│   ├── architecture.md              # System architecture
│   ├── api.md                       # API documentation
│   └── deployment.md                # Deployment guide
│
├── docker-compose.yml               # Multi-container setup
├── .env.example                     # Environment template
├── .gitignore                       # Git configuration
└── README.md                        # This file
```

---

## Screenshots & UI

### Dashboard
![Dashboard](docs/screenshots/dashboard.png)
*Main dashboard showing recent analyses and quick stats*

### Resume Upload & Analysis
![Resume Upload](docs/screenshots/resume-upload.png)
*Drag-and-drop resume upload with file preview*

### Analysis Results
![Analysis Results](docs/screenshots/analysis-results.png)
*Detailed score breakdown with matched/missing skills*

### Score Visualization
![Score Breakdown](docs/screenshots/score-breakdown.png)
*6-dimension score visualization with interactive charts*

### Optimization View
![Optimization](docs/screenshots/optimization.png)
*Before/after comparison with highlighted improvements*

---

## Security

### Authentication & Authorization
- **JWT Tokens:** Stateless authentication with 24-hour expiration
- **Refresh Tokens:** Rotate tokens securely
- **Role-Based Access Control:** User, Recruiter, Admin, Enterprise Admin roles
- **OAuth2 Integration:** Sign in with Google, GitHub (optional)

### Data Protection
- **Encryption in Transit:** TLS 1.3 for all connections
- **Password Security:** bcrypt hashing with 12+ rounds
- **PII Handling:** Encrypted storage of sensitive personal information
- **File Security:** Secure upload directory with access controls

### Audit & Compliance
- **Audit Logging:** Complete activity trail with timestamps and IP addresses
- **GDPR Compliance:** Data export and deletion capabilities
- **SOC2 Ready:** Structured logging for compliance audits
- **HIPAA Compatible:** Optional encryption and compliance modes

### API Security
- **Rate Limiting:** Prevent abuse and DDoS attacks
- **Input Validation:** All inputs sanitized and validated
- **CORS:** Restricted to configured origins
- **CSRF Protection:** Token-based CSRF prevention

---

## Deployment

### Docker Deployment
```bash
# Build images
docker-compose build

# Start services
docker-compose up -d

# View logs
docker-compose logs -f backend
```

### Kubernetes Deployment
```bash
# Create namespace
kubectl create namespace hiremind

# Deploy manifests
kubectl apply -f k8s/ -n hiremind

# Check status
kubectl get pods -n hiremind
```

### Cloud Deployment
- **AWS:** Deploy to ECS, RDS, and ALB
- **Google Cloud:** Cloud Run for backend, Cloud SQL for database
- **Azure:** App Service, Azure Database for PostgreSQL
- **Heroku:** Buildpack deployment ready

See [docs/deployment.md](docs/deployment.md) for detailed guides.

---

## Performance & Scalability

### Performance Metrics
- **API Response Time:** < 200ms (p99)
- **Resume Upload:** Handles 100MB files
- **Analysis Generation:** 30-60 seconds per resume
- **Concurrent Users:** 1000+ supported per instance

### Scalability Features
- Horizontal scaling of backend services
- Database read replicas
- Redis caching for sessions
- Async job processing for long operations
- CDN-friendly static assets

---

## Future Roadmap

### Q2 2026
- Multi-language resume support (Spanish, French, German)
- Bulk resume processing for recruiters
- LinkedIn profile integration for auto-import

### Q3 2026
- Video interview analysis and transcript parsing
- AI interview preparation mode
- Custom scoring weights per role

### Q4 2026
- Recruiter dashboard with batch candidate analysis
- Analytics dashboard with hiring insights
- Mobile applications (iOS/Android)

### 2027+
- Predictive match scoring using historical data
- Resume template library with ATS-optimized designs
- Custom ML model training for industry-specific scoring
- Integration with popular ATS systems (Workday, Greenhouse, Lever)

---

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Testing
```bash
# Run backend tests
mvn test

# Run frontend tests
npm test

# Generate coverage reports
mvn test jacoco:report
npm test -- --coverage
```

---

## Support & Documentation

- **Documentation:** [docs/](docs/) directory
- **API Docs:** Swagger UI at `/api/swagger-ui.html`
- **Issues:** [GitHub Issues](https://github.com/yourusername/hiremind-ai/issues)
- **Email:** support@hiremind.ai
- **Community:** [Discord](https://discord.gg/hiremind)

---

## License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- OpenAI for GPT-4 API
- Anthropic for Claude
- Spring Boot community
- React ecosystem
- PostgreSQL for excellent database

---

## Author

**HireMind AI Development Team**
- Website: https://hiremind.ai
- Twitter: [@HireMindAI](https://twitter.com/HireMindAI)
- LinkedIn: [HireMind AI](https://linkedin.com/company/hiremind-ai)

---

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=yourusername/hiremind-ai&type=Date)](https://star-history.com/#yourusername/hiremind-ai&Date)

---

**Made with ❤️ by the HireMind AI team**
