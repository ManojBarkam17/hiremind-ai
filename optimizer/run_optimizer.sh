#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# HireMind AI — Resume Optimizer Pipeline
# Usage: ./run_optimizer.sh <resume.txt> <jobdesc.txt> [output_name]
# ─────────────────────────────────────────────────────────────────────────────

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

RESUME="${1:-sample_resume.txt}"
JOBDESC="${2:-sample_jobdesc.txt}"
OUTPUT_NAME="${3:-optimized_resume}"

echo "╔══════════════════════════════════════════════════════════════╗"
echo "║           HireMind AI — Resume Optimizer                     ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo ""

# Step 1: Run Python optimizer
echo "🔍 Step 1/3: Analyzing & optimizing resume..."
python3 resume_optimizer.py "$RESUME" "$JOBDESC" optimized_result.json
echo ""

# Step 2: Generate DOCX
echo "📄 Step 2/3: Generating Word document..."
node generate_docx.js optimized_result.json "${OUTPUT_NAME}.docx"
echo ""

# Step 3: Generate PDF
echo "📕 Step 3/3: Generating PDF document..."
python3 generate_pdf.py optimized_result.json "${OUTPUT_NAME}.pdf"
echo ""

echo "╔══════════════════════════════════════════════════════════════╗"
echo "║  ✅ Done! Your optimized resume is ready:                    ║"
echo "║     📄 ${OUTPUT_NAME}.docx                                   ║"
echo "║     📕 ${OUTPUT_NAME}.pdf                                    ║"
echo "╚══════════════════════════════════════════════════════════════╝"
