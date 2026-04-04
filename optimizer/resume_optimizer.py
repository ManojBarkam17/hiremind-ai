#!/usr/bin/env python3
"""
HireMind AI — Resume Optimizer Engine
Compares resume against job description and generates an ATS-optimized version.
Outputs: optimized_resume.json (structured data for DOCX/PDF generation)
"""

import json
import re
import sys
import os
from collections import Counter

# ─── SKILL DATABASE ──────────────────────────────────────────────────────────
TECH_SKILLS = {
    # Programming Languages
    "python", "java", "javascript", "typescript", "c++", "c#", "go", "rust", "ruby",
    "php", "swift", "kotlin", "scala", "r", "matlab", "perl", "bash", "shell",
    "sql", "nosql", "html", "css", "sass", "less",
    # Frameworks & Libraries
    "react", "angular", "vue", "vue.js", "next.js", "nuxt.js", "node.js", "express",
    "django", "flask", "fastapi", "spring", "spring boot", "hibernate", ".net",
    "asp.net", "laravel", "rails", "ruby on rails", "tensorflow", "pytorch",
    "scikit-learn", "pandas", "numpy", "matplotlib", "keras",
    # Cloud & DevOps
    "aws", "azure", "gcp", "google cloud", "docker", "kubernetes", "k8s",
    "terraform", "ansible", "jenkins", "github actions", "gitlab ci", "ci/cd",
    "linux", "unix", "nginx", "apache", "cloudformation", "helm",
    # Databases
    "mysql", "postgresql", "postgres", "mongodb", "redis", "elasticsearch",
    "dynamodb", "cassandra", "oracle", "sql server", "sqlite", "firebase",
    # Tools & Practices
    "git", "jira", "confluence", "agile", "scrum", "kanban", "rest", "restful",
    "graphql", "microservices", "api", "apis", "oauth", "jwt", "sso",
    "machine learning", "deep learning", "nlp", "computer vision", "ai",
    "artificial intelligence", "data science", "data engineering", "etl",
    "data pipeline", "spark", "hadoop", "kafka", "airflow",
    # Testing
    "junit", "jest", "pytest", "selenium", "cypress", "testing", "unit testing",
    "integration testing", "tdd", "bdd",
    # Other
    "figma", "sketch", "adobe xd", "tableau", "power bi", "excel",
    "project management", "leadership", "communication", "problem solving",
} round(skill_score, 1),
            "preferred_skills": round(preferred_score, 1),
            "semantic_similarity": round(semantic_score, 1),
            "domain_alignment": round(domain_score, 1),
            "ats_keywords": round(ats_keyword_score, 1),
            "formatting": round(formatting_score, 1),
        },
        "matched_skills": sorted(matched_skills),
        "missing_skills": sorted(missing_skills),
        "extra_skills": sorted(resume_skills - jd_skills),
    }


def extract_domain_terms(text):
    """Extract industry/domain-specific terms."""
    patterns = [
        r'\b(?:SaaS|B2B|B2C|fintech|healthtech|edtech|e-commerce)\b',
        r'\b(?:enterprise|startup|Fortune 500|Series [A-D])\b',
        r'\b(?:compliance|HIPAA|GDPR|SOC2|PCI|ISO)\b',
        r'\b(?:scalability|high availability|distributed|real-time)\b',
        r'\b(?:revenue|growth|retention|conversion|engagement)\b',
    ]
    terms = []
    for pat in patterns:
        terms.extend(re.findall(pat, text, re.IGNORECASE))
    return terms


def evaluate_formatting(text):
    """Evaluate resume formatting for ATS compatibility."""
    score = 100
    lines = text.strip().split('\n')

    # Check for common ATS issues
    if len(lines) < 10:
        score -= 20  # Too short
    if not any(c.isdigit() for c in text):
        score -= 15  # No metrics/numbers
    if len(text) > 10000:
        score -= 10  # Too long
    if len(text) < 500:
        score -= 15  # Too short
    # Check for section headers
    sections = ['experience', 'education', 'skills', 'summary', 'objective']
    found_sections = sum(1 for s in sections if s in text.lower())
    if found_sections < 3:
        score -= 15

    return max(0, min(100, score))


def parse_resume_sections(text):
    """Parse resume into structured sections."""
    sections = {
        "contact": "",
        "summary": "",
        "experience": [],
        "education": [],
        "skills": [],
        "certifications": [],
        "projects": [],
    }

    lines = text.strip().split('\n')
    current_section = "contact"
    current_block = []

    section_headers = {
        'summary': ['summary', 'objective', 'profile', 'about'],
        'experience': ['experience', 'work history', 'employment', 'professional experience', 'work experience'],
        'education': ['education', 'academic', 'qualifications', 'degree'],
        'skills': ['skills', 'technical skills', 'core competencies', 'technologies', 'tools'],
        'certifications': ['certifications', 'certificates', 'licenses', 'credentials'],
        'projects': ['projects', 'key projects', 'notable projects'],
    }

    for line in lines:
        line_stripped = line.strip()
        if not line_stripped:
            continue

        # Check if this line is a section header
        line_lower = line_stripped.lower().rstrip(':').strip()
        matched_section = None
        for section, keywords in section_headers.items():
            if line_lower in keywords or any(kw in line_lower for kw in keywords):
                matched_section = section
                break

        if matched_section:
            # Save previous block
            if current_block:
                if current_section in ['experience', 'education', 'projects']:
                    sections[current_section].append('\n'.join(current_block))
                elif current_section == 'skills':
                    sections['skills'].extend(current_block)
                else:
                    sections[current_section] = '\n'.join(current_block)
            current_section = matched_section
            current_block = []
        else:
            current_block.append(line_stripped)

    # Save last block
    if current_block:
        if current_section in ['experience', 'education', 'projects']:
            sections[current_section].append('\n'.join(current_block))
        elif current_section == 'skills':
            sections['skills'].extend(current_block)
        else:
            sections[current_section] = '\n'.join(current_block)

    return sections


def optimize_resume(resume_text, jd_text):
    """Main optimization: analyze, enhance, and restructure resume."""
    # Score BEFORE optimization
    before_score = calculate_ats_score(resume_text, jd_text)

    # Parse resume
    sections = parse_resume_sections(resume_text)

    # Extract JD requirements
    jd_skills = extract_skills(jd_text)
    resume_skills = extract_skills(resume_text)
    missing = jd_skills - resume_skills
    matched = jd_skills & resume_skills

    jd_keywords = extract_keywords(jd_text, 25)
    jd_keyword_list = [kw for kw, _ in jd_keywords]

    # ─── OPTIMIZE SUMMARY ────────────────────────────────────────────────
    optimized_summary = optimize_summary(
        sections.get('summary', ''), jd_text, matched, missing, jd_keyword_list
    )

    # ─── OPTIMIZE EXPERIENCE ─────────────────────────────────────────────
    optimized_experience = optimize_experience(
        sections.get('experience', []), jd_text, missing, jd_keyword_list
    )

    # ─── OPTIMIZE SKILLS ─────────────────────────────────────────────────
    optimized_skills = optimize_skills(
        sections.get('skills', []), matched, missing
    )

    # ─── BUILD OPTIMIZED RESUME ──────────────────────────────────────────
    optimized = {
        "contact": sections.get('contact', ''),
        "summary": optimized_summary,
        "experience": optimized_experience,
        "education": sections.get('education', []),
        "skills": optimized_skills,
        "certifications": sections.get('certifications', []),
        "projects": sections.get('projects', []),
    }

    # Calculate AFTER score
    optimized_text = build_plain_text(optimized)
    after_score = calculate_ats_score(optimized_text, jd_text)

    return {
        "before_score": before_score,
        "after_score": after_score,
        "score_improvement": round(after_score['overall_score'] - before_score['overall_score'], 1),
        "optimized_resume": optimized,
        "changes_made": generate_change_log(before_score, after_score, missing),
    }


def optimize_summary(summary, jd_text, matched, missing, keywords):
    """Create an ATS-optimized professional summary."""
    # Extract role title from JD
    role_match = re.search(r'(?:title|role|position)[:\s]*([^\n]+)', jd_text, re.I)
    role = role_match.group(1).strip() if role_match else "Software Professional"

    # Build keyword-rich summary
    top_skills = sorted(matched)[:5]
    missing_to_add = sorted(missing)[:3]

    if summary:
        optimized = summary.strip()
        # Inject missing keywords naturally
        for skill in missing_to_add:
            if skill.lower() not in optimized.lower():
                optimized += f" Proficient in {skill}."
    else:
        skill_str = ", ".join(top_skills[:4]) if top_skills else "modern technologies"
        optimized = (
            f"Results-driven {role} with extensive experience in {skill_str}. "
            f"Proven track record of delivering scalable, high-performance solutions "
            f"that drive business impact."
        )
        for skill in missing_to_add:
            optimized += f" Experienced with {skill}."

    return optimized


def optimize_experience(experience_blocks, jd_text, missing, keywords):
    """Enhance experience bullets with ATS keywords and metrics."""
    optimized = []
    missing_list = list(missing)
    verb_idx = 0

    for block in experience_blocks:
        lines = block.split('\n')
        enhanced_lines = []
        for i, line in enumerate(lines):
            line = line.strip()
            if not line:
                continue
            # Check if it's a bullet point or description line
            if line.startswith(('-', '•', '*', '–')) or (i > 0 and not line[0].isupper()):
                # Enhance bullet
                bullet = line.lstrip('-•*– ').strip()
                # Add action verb if missing
                if bullet and not any(bullet.startswith(v) for v in ACTION_VERBS):
                    verb = ACTION_VERBS[verb_idx % len(ACTION_VERBS)]
                    verb_idx += 1
                    if bullet[0].islower():
                        bullet = f"{verb} {bullet}"
                    else:
                        bullet = f"{verb} {bullet[0].lower()}{bullet[1:]}"
                # Inject a missing skill if natural
                if missing_list and len(enhanced_lines) > 0:
                    skill = missing_list[0]
                    if skill.lower() not in bullet.lower() and len(bullet) < 150:
                        bullet += f", leveraging {skill}"
                        missing_list.pop(0)
                enhanced_lines.append(bullet)
            else:
                enhanced_lines.append(line)
        optimized.append('\n'.join(enhanced_lines))

    return optimized


def optimize_skills(skills, matched, missing):
    """Restructure skills section to prioritize JD-matched skills."""
    all_skills = set()
    for s in skills:
        # Split by common delimiters
        parts = re.split(r'[,;|•\-]', s)
        for p in parts:
            p = p.strip()
            if p and len(p) > 1:
                all_skills.add(p)

    # Add matched and missing skills
    all_skills |= matched
    all_skills |= missing  # Add missing JD skills — candidate should verify these

    # Sort: matched first, then missing (flagged), then extras
    categorized = {
        "matched": sorted(matched),
        "added_from_jd": sorted(missing),
        "existing": sorted(all_skills - matched - missing),
    }

    # Flatten into a single list with matched skills first
    flat = categorized["matched"] + categorized["added_from_jd"] + categorized["existing"]
    # Remove duplicates preserving order
    seen = set()
    unique = []
    for s in flat:
        sl = s.lower()
        if sl not in seen:
            seen.add(sl)
            unique.append(s)

    return unique


def build_plain_text(optimized):
    """Build plain text version of optimized resume for scoring."""
    parts = []
    if optimized['contact']:
        parts.append(optimized['contact'])
    parts.append("\nSummary")
    parts.append(optimized['summary'])
    parts.append("\nExperience")
    for exp in optimized['experience']:
        parts.append(exp)
    parts.append("\nEducation")
    for edu in optimized['education']:
        parts.append(edu)
    parts.append("\nSkills")
    parts.append(', '.join(optimized['skills']))
    return '\n'.join(parts)


def generate_change_log(before, after, missing):
    """Generate a list of changes made."""
    changes = []
    if after['overall_score'] > before['overall_score']:
        changes.append(f"Overall ATS score improved from {before['overall_score']}% to {after['overall_score']}%")

    for dim in before['dimensions']:
        b = before['dimensions'][dim]
        a = after['dimensions'][dim]
        if a > b:
            label = dim.replace('_', ' ').title()
            changes.append(f"{label}: {b}% → {a}% (+{round(a-b,1)}%)")

    if missing:
        changes.append(f"Added {len(missing)} missing skills from job description: {', '.join(sorted(missing)[:8])}")

    changes.append("Enhanced experience bullets with action verbs and metrics")
    changes.append("Restructured skills section to prioritize JD-matched keywords")
    changes.append("Optimized summary with role-specific keywords")

    return changes


# ─── MAIN ────────────────────────────────────────────────────────────────────

def main():
    if len(sys.argv) < 3:
        print("Usage: python resume_optimizer.py <resume.txt> <jobdesc.txt>")
        sys.exit(1)

    resume_file = sys.argv[1]
    jd_file = sys.argv[2]
    output_file = sys.argv[3] if len(sys.argv) > 3 else "optimized_result.json"

    with open(resume_file, 'r') as f:
        resume_text = f.read()
    with open(jd_file, 'r') as f:
        jd_text = f.read()

    print("🔍 Analyzing resume against job description...")
    result = optimize_resume(resume_text, jd_text)

    print(f"\n📊 ATS Score: {result['before_score']['overall_score']}% → {result['after_score']['overall_score']}%")
    print(f"📈 Improvement: +{result['score_improvement']}%")
    print(f"\n✅ Matched Skills: {', '.join(result['before_score']['matched_skills'][:10])}")
    print(f"❌ Missing Skills (added): {', '.join(result['before_score']['missing_skills'][:10])}")
    print(f"\n📝 Changes Made:")
    for change in result['changes_made']:
        print(f"   • {change}")

    with open(output_file, 'w') as f:
        json.dump(result, f, indent=2)

    print(f"\n✅ Optimized result saved to {output_file}")
    return result


if __name__ == "__main__":
    main()
