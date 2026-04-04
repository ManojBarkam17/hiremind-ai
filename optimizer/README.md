# HireMind AI — Resume Optimizer Tool

A powerful CLI-based resume optimization engine that compares your resume against a job description, calculates an ATS (Applicant Tracking System) compatibility score across 6 dimensions, and generates an optimized, ATS-friendly resume in both **Word (.docx)** and **PDF** formats.

## Features

- **6-Dimension ATS Scoring**: Required Skills (35%), Preferred Skills (15%), Semantic Similarity (15%), Domain Alignment (10%), ATS Keywords (15%), Formatting (10%)
- **Smart Skill Extraction**: 100+ predefined tech and soft skills database
- **Keyword Optimization**: Automatically injects missing JD keywords into your resume
- **Action Verb Enhancement**: Upgrades experience bullets with strong action verbs
- **Dual Output**: Generates both `.docx` and `.pdf` with professional formatting
- **Change Log**: Shows exactly what was improved and by how much

## Quick Start

### Prerequisites

```bash
# Python packages
pip install reportlab

# Node.js package
cd optimizer
npm install
```

### Usage

```bash
# Using the pipeline script
./run_optimizer.sh resume.txt jobdesc.txt output_name

# Or run each step manually
python3 resume_optimizer.py resume.txt jobdesc.txt optimized_result.json
node generate_docx.js optimized_result.json optimized_resume.docx
python3 generate_pdf.py optimized_result.json optimized_resume.pdf
```

### Example

```bash
./run_optimizer.sh sample_resume.txt sample_jobdesc.txt my_optimized_resume
```

This will generate:
- `my_optimized_resume.docx` — ATS-friendly Word document
- `my_optimized_resume.pdf` — ATS-friendly PDF document
- `optimized_result.json` — Full analysis with scores and changes

## How It Works

1. **Parse**: Extracts sections from your resume (contact, summary, experience, education, skills, certifications, projects)
2. **Analyze**: Compares resume skills and keywords against the job description
3. **Score**: Calculates ATS compatibility across 6 weighted dimensions
4. **Optimize**: Enhances summary with role-specific keywords, adds missing skills, upgrades bullets with action verbs
5. **Generate**: Produces professionally formatted DOCX and PDF outputs

## File Structure

```
optimizer/
├── resume_optimizer.py    # Core optimization engine (Python)
├── generate_docx.js       # Word document generator (Node.js/docx-js)
├── generate_pdf.py        # PDF generator (Python/reportlab)
├── run_optimizer.sh       # Pipeline orchestrator script
├── sample_resume.txt      # Sample resume for testing
├── sample_jobdesc.txt     # Sample job description for testing
├── package.json           # Node.js dependencies
└── README.md              # This file
```
