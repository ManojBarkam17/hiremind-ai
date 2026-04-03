package com.hiremind.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResumeParserService {

    public String extractText(byte[] fileContent, String contentType) throws IOException {
        if (contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }

        if (contentType.contains("pdf")) {
            return extractFromPdf(fileContent);
        } else if (contentType.contains("wordprocessingml") || contentType.contains("msword")) {
            return extractFromDocx(fileContent);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
    }

    private String extractFromPdf(byte[] fileContent) throws IOException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(fileContent))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromDocx(byte[] fileContent) throws IOException {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(fileContent))) {
            return document.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining("\n"));
        }
    }
}
