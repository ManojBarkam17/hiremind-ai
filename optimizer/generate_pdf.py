#!/usr/bin/env python3
"""
HireMind AI — PDF Resume Generator
Generates a professional ATS-friendly PDF from optimized resume JSON.
"""

import json
import sys
import os
import textwrap

from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.colors import HexColor
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_JUSTIFY
from reportlab.lib.units import inch
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, HRFlowable, ListFlowable, ListItem
)


def build_pdf(result, output_file="optimized_resume.pdf"):
    """Build a professional ATS-friendly PDF resume."""
    resume = result["optimized_resume"]

    doc = SimpleDocTemplate(
        output_file,
        pagesize=letter,
        topMargin=0.75 * inch,
        bottomMargin=0.75 * inch,
        leftMargin=0.75 * inch,
        rightMargin=0.75 * inch,
    )

    # ─── STYLES ───────────────────────────────────────────────────────────
    styles = getSampleStyleSheet()

    style_name = ParagraphStyle(
        "ResumeName",
        parent=styles["Title"],
        fontName="Helvetica-Bold",
        fontSize=22,
        leading=26,
        alignment=TA_CENTER,
        textColor=HexColor("#1A1A1A"),
        spaceAfter=4,
    )

    style_contact = ParagraphStyle(
        "ResumeContact",
        parent=styles["Normal"],
        fontName="Helvetica",
        fontSize=9.5,
        leading=13,
        alignment=TA_CENTER,
        textColor=HexColor("#555555"),
        spaceAfter=16,
    )

    style_section = ParagraphStyle(
        "SectionHeading",
        parent=styles["Heading1"],
        fontName="Helvetica-Bold",
        fontSize=12,
        leading=16,
        textColor=HexColor("#2B579A"),
        spaceBefore=14,
        spaceAfter=6,
        borderWidth=0,
    )

    style_body = ParagraphStyle(
        "ResumeBody",
        parent=styles["Normal"],
        fontName="Helvetica",
        fontSize=10,
        leading=14,
        textColor=HexColor("#333333"),
        alignment=TA_JUSTIFY,
        spaceAfter=6,
    )

    style_job_title = ParagraphStyle(
        "JobTitle",
        parent=styles["Normal"],
        fontName="Helvetica-Bold",
        fontSize=10.5,
        leading=14,
        textColor=HexColor("#1A1A1A"),
        spaceBefore=10,
        spaceAfter=3,
    )

    style_bullet = ParagraphStyle(
        "BulletItem",
        parent=styles["Normal"],
        fontName="Helvetica",
        fontSize=10,
        leading=13,
        textColor=HexColor("#333333"),
        leftIndent=18,
        spaceAfter=3,
    )

    style_skill = ParagraphStyle(
        "SkillText",
        parent=styles["Normal"],
        fontName="Helvetica",
        fontSize=10,
        leading=14,
        textColor=HexColor("#333333"),
        spaceAfter=8,
    )

    style_footer = ParagraphStyle(
        "FooterNote",
        parent=styles["Normal"],
        fontName="Helvetica-Oblique",
        fontSize=8,
        leading=10,
        alignment=TA_CENTER,
        textColor=HexColor("#999999"),
        spaceBefore=20,
    )

    # ─── BUILD CONTENT ────────────────────────────────────────────────────
    story = []

    # Contact / Header
    if resume.get("contact"):
        contact_lines = [l.strip() for l in resume["contact"].split("\n") if l.strip()]
        if contact_lines:
            story.append(Paragraph(contact_lines[0], style_name))
            if len(contact_lines) > 1:
                story.append(Paragraph("  |  ".join(contact_lines[1:]), style_contact))

    # Section divider helper
    def add_section_divider():
        story.append(HRFlowable(
            width="100%", thickness=1, color=HexColor("#2B579A"),
            spaceAfter=4, spaceBefore=2
        ))

    # ─── SUMMARY ──────────────────────────────────────────────────────────
    if resume.get("summary"):
        story.append(Paragraph("PROFESSIONAL SUMMARY", style_section))
        add_section_divider()
        story.append(Paragraph(resume["summary"], style_body))

    # ─── EXPERIENCE ───────────────────────────────────────────────────────
    if resume.get("experience") and len(resume["experience"]) > 0:
        story.append(Paragraph("PROFESSIONAL EXPERIENCE", style_section))
        add_section_divider()
        for block in resume["experience"]:
            lines = [l.strip() for l in block.split("\n") if l.strip()]
            for i, line in enumerate(lines):
                if i == 0:
                    story.append(Paragraph(line, style_job_title))
                else:
                    # Bullet point with bullet character
                    clean = line.lstrip("-*• ").strip()
                    story.append(Paragraph(f"• {clean}", style_bullet))

    # ─── EDUCATION ────────────────────────────────────────────────────────
    if resume.get("education") and len(resume["education"]) > 0:
        story.append(Paragraph("EDUCATION", style_section))
        add_section_divider()
        for block in resume["education"]:
            lines = [l.strip() for l in block.split("\n") if l.strip()]
            for line in lines:
                story.append(Paragraph(line, style_body))

    # ─── SKILLS ───────────────────────────────────────────────────────────
    if resume.get("skills") and len(resume["skills"]) > 0:
        story.append(Paragraph("TECHNICAL SKILLS", style_section))
        add_section_divider()
        skill_text = "  •  ".join(resume["skills"])
        story.append(Paragraph(skill_text, style_skill))

    # ─── CERTIFICATIONS ───────────────────────────────────────────────────
    if resume.get("certifications") and len(resume["certifications"]) > 0:
        story.append(Paragraph("CERTIFICATIONS", style_section))
        add_section_divider()
        for cert in resume["certifications"]:
            lines = [l.strip() for l in cert.split("\n") if l.strip()]
            for line in lines:
                story.append(Paragraph(f"• {line}", style_bullet))

    # ─── PROJECTS ─────────────────────────────────────────────────────────
    if resume.get("projects") and len(resume["projects"]) > 0:
        story.append(Paragraph("PROJECTS", style_section))
        add_section_divider()
        for block in resume["projects"]:
            lines = [l.strip() for l in block.split("\n") if l.strip()]
            for i, line in enumerate(lines):
                if i == 0:
                    story.append(Paragraph(line, style_job_title))
                else:
                    clean = line.lstrip("-*• ").strip()
                    story.append(Paragraph(f"• {clean}", style_bullet))

    # ─── ATS SCORE FOOTER ─────────────────────────────────────────────────
    story.append(Spacer(1, 20))
    story.append(HRFlowable(width="100%", thickness=0.5, color=HexColor("#CCCCCC"), spaceAfter=6))
    score_before = result["before_score"]["overall_score"]
    score_after = result["after_score"]["overall_score"]
    story.append(Paragraph(
        f"ATS Score: {score_before}% → {score_after}% | Optimized by HireMind AI",
        style_footer
    ))

    # ─── BUILD PDF ────────────────────────────────────────────────────────
    doc.build(story)
    print(f"✅ PDF generated: {output_file}")


# ─── MAIN ─────────────────────────────────────────────────────────────────────

def main():
    input_file = sys.argv[1] if len(sys.argv) > 1 else "optimized_result.json"
    output_file = sys.argv[2] if len(sys.argv) > 2 else "optimized_resume.pdf"

    if not os.path.exists(input_file):
        print(f"Error: {input_file} not found. Run resume_optimizer.py first.")
        sys.exit(1)

    with open(input_file, "r") as f:
        result = json.load(f)

    build_pdf(result, output_file)


if __name__ == "__main__":
    main()
