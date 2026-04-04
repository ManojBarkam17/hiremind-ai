#!/usr/bin/env node
/**
 * HireMind AI — DOCX Resume Generator
 * Generates a professional ATS-friendly Word document from optimized resume JSON.
 */

const fs = require('fs');
const {
  Document, Packer, Paragraph, TextRun, HeadingLevel,
  AlignmentType, LevelFormat, BorderStyle, TabStopType
} = require('docx');

// Read optimized result JSON
const inputFile = process.argv[2] || 'optimized_result.json';
if (!fs.existsSync(inputFile)) {
  console.error(`Error: ${inputFile} not found. Run resume_optimizer.py first.`);
  process.exit(1);
}

const result = JSON.parse(fs.readFileSync(inputFile, 'utf-8'));
const resume = result.optimized_resume;
const outputFile = process.argv[3] || 'optimized_resume.docx';

// ─── HELPERS ────────────────────────────────────────────────────────────────

function sectionHeading(text) {
  return new Paragraph({
    heading: HeadingLevel.HEADING_1,
    spacing: { before: 300, after: 100 },
    border: { bottom: { style: BorderStyle.SINGLE, size: 1, color: "2B579A" } },
    children: [new TextRun({ text: text.toUpperCase(), bold: true, font: "Arial", size: 24, color: "2B579A" })],
  });
}

function bulletItem(text) {
  return new Paragraph({
    numbering: { reference: "bullets", level: 0 },
    spacing: { before: 40, after: 40 },
    children: [new TextRun({ text, font: "Arial", size: 21 })],
  });
}

function normalParagraph(text, opts = {}) {
  return new Paragraph({
    spacing: { before: opts.spaceBefore || 0, after: opts.spaceAfter || 60 },
    alignment: opts.alignment || AlignmentType.LEFT,
    children: [new TextRun({
      text,
      font: "Arial",
      size: opts.size || 21,
      bold: opts.bold || false,
      italics: opts.italic || false,
      color: opts.color || "333333",
    })],
  });
}

// ─── BUILD DOCUMENT CONTENT ──────────────────────────────────────────────────

const children = [];

// ─── CONTACT / HEADER ────────────────────────────────────────────────────────
if (resume.contact) {
  const contactLines = resume.contact.split('\n').filter(l => l.trim());
  if (contactLines.length > 0) {
    // Name (first line, large)
    children.push(new Paragraph({
      alignment: AlignmentType.CENTER,
      spacing: { after: 60 },
      children: [new TextRun({ text: contactLines[0], font: "Arial", size: 36, bold: true, color: "1A1A1A" })],
    }));
    // Contact details (remaining lines)
    if (contactLines.length > 1) {
      children.push(new Paragraph({
        alignment: AlignmentType.CENTER,
        spacing: { after: 200 },
        children: [new TextRun({
          text: contactLines.slice(1).join('  |  '),
          font: "Arial", size: 19, color: "555555",
        })],
      }));
    }
  }
}

// ─── SUMMARY ─────────────────────────────────────────────────────────────────
if (resume.summary) {
  children.push(sectionHeading("Professional Summary"));
  children.push(normalParagraph(resume.summary, { spaceAfter: 120 }));
}

// ─── EXPERIENCE ──────────────────────────────────────────────────────────────
if (resume.experience && resume.experience.length > 0) {
  children.push(sectionHeading("Professional Experience"));
  for (const block of resume.experience) {
    const lines = block.split('\n').filter(l => l.trim());
    for (let i = 0; i < lines.length; i++) {
      const line = lines[i].trim();
      if (i === 0) {
        // Job title / company line — bold
        children.push(normalParagraph(line, { bold: true, spaceBefore: 120, spaceAfter: 40, size: 22, color: "1A1A1A" }));
      } else {
        // Bullet points
        children.push(bulletItem(line));
      }
    }
  }
}

// ─── EDUCATION ───────────────────────────────────────────────────────────────
if (resume.education && resume.education.length > 0) {
  children.push(sectionHeading("Education"));
  for (const block of resume.education) {
    const lines = block.split('\n').filter(l => l.trim());
    for (const line of lines) {
      children.push(normalParagraph(line, { spaceAfter: 40 }));
    }
  }
}

// ─── SKILLS ──────────────────────────────────────────────────────────────────
if (resume.skills && resume.skills.length > 0) {
  children.push(sectionHeading("Technical Skills"));
  // Group skills into rows of ~6 for readability
  const skillText = resume.skills.join('  •  ');
  children.push(normalParagraph(skillText, { spaceAfter: 100 }));
}

// ─── CERTIFICATIONS ──────────────────────────────────────────────────────────
if (resume.certifications && resume.certifications.length > 0) {
  children.push(sectionHeading("Certifications"));
  for (const cert of resume.certifications) {
    const lines = cert.split('\n').filter(l => l.trim());
    for (const line of lines) {
      children.push(bulletItem(line));
    }
  }
}

// ─── PROJECTS ────────────────────────────────────────────────────────────────
if (resume.projects && resume.projects.length > 0) {
  children.push(sectionHeading("Projects"));
  for (const block of resume.projects) {
    const lines = block.split('\n').filter(l => l.trim());
    for (let i = 0; i < lines.length; i++) {
      const line = lines[i].trim();
      if (i === 0) {
        children.push(normalParagraph(line, { bold: true, spaceBefore: 80, spaceAfter: 40, size: 22 }));
      } else {
        children.push(bulletItem(line));
      }
    }
  }
}

// ─── ATS SCORE FOOTER ────────────────────────────────────────────────────────
children.push(new Paragraph({
  spacing: { before: 400 },
  border: { top: { style: BorderStyle.SINGLE, size: 1, color: "CCCCCC" } },
  children: [],
}));
children.push(new Paragraph({
  alignment: AlignmentType.CENTER,
  spacing: { before: 100 },
  children: [new TextRun({
    text: `ATS Score: ${result.before_score.overall_score}% → ${result.after_score.overall_score}% | Optimized by HireMind AI`,
    font: "Arial", size: 16, italics: true, color: "999999",
  })],
}));

// ─── CREATE DOCUMENT ─────────────────────────────────────────────────────────

const doc = new Document({
  styles: {
    default: {
      document: { run: { font: "Arial", size: 21 } },
    },
    paragraphStyles: [
      {
        id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 24, bold: true, font: "Arial", color: "2B579A" },
        paragraph: { spacing: { before: 300, after: 100 }, outlineLevel: 0 },
      },
    ],
  },
  numbering: {
    config: [
      {
        reference: "bullets",
        levels: [{
          level: 0, format: LevelFormat.BULLET, text: "\u2022",
          alignment: AlignmentType.LEFT,
          style: { paragraph: { indent: { left: 720, hanging: 360 } } },
        }],
      },
    ],
  },
  sections: [{
    properties: {
      page: {
        size: { width: 12240, height: 15840 },
        margin: { top: 1080, right: 1080, bottom: 1080, left: 1080 },
      },
    },
    children,
  }],
});

// ─── EXPORT ──────────────────────────────────────────────────────────────────

Packer.toBuffer(doc).then(buffer => {
  fs.writeFileSync(outputFile, buffer);
  console.log(`✅ DOCX generated: ${outputFile}`);
}).catch(err => {
  console.error('Error generating DOCX:', err);
  process.exit(1);
});
