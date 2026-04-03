package com.hiremind.resume.service;

import com.hiremind.ai.service.ResumeParserService;
import com.hiremind.common.exception.ResourceNotFoundException;
import com.hiremind.common.exception.ValidationException;
import com.hiremind.resume.dto.ResumeUploadResponse;
import com.hiremind.resume.entity.Resume;
import com.hiremind.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeParserService resumeParserService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final String[] ALLOWED_TYPES = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

    public ResumeUploadResponse uploadResume(UUID userId, MultipartFile file, boolean setAsPrimary) {
        // Validate file
        validateFile(file);

        try {
            // Read file content
            byte[] fileContent = file.getBytes();
            String rawText = resumeParserService.extractText(fileContent, file.getContentType());

            // Create resume entity
            Resume resume = Resume.builder()
                .userId(userId)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .content(new String(fileContent))
                .rawText(rawText)
                .status("PARSED")
                .isPrimary(setAsPrimary)
                .build();

            // If setting as primary, unset others
            if (setAsPrimary) {
                List<Resume> userResumes = resumeRepository.findByUserId(userId);
                userResumes.forEach(r -> r.setIsPrimary(false));
                resumeRepository.saveAll(userResumes);
            }

            resume = resumeRepository.save(resume);
            log.info("Resume uploaded successfully for user: {} - File: {}", userId, file.getOriginalFilename());

            return ResumeUploadResponse.builder()
                .resumeId(resume.getId())
                .fileName(resume.getFileName())
                .fileSize(resume.getFileSize())
                .status(resume.getStatus())
                .isPrimary(resume.getIsPrimary())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .message("Resume uploaded and parsed successfully")
                .build();

        } catch (IOException e) {
            log.error("Error reading file: {}", file.getOriginalFilename(), e);
            throw new ValidationException("Error reading file: " + e.getMessage());
        }
    }

    public List<Resume> getUserResumes(UUID userId) {
        return resumeRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Resume getResume(UUID resumeId, UUID userId) {
        return resumeRepository.findByIdAndUserId(resumeId, userId)
            .orElseThrow(() -> ResourceNotFoundException.forEntity("Resume", resumeId.toString()));
    }

    public void deleteResume(UUID resumeId, UUID userId) {
        Resume resume = getResume(resumeId, userId);
        resumeRepository.delete(resume);
        log.info("Resume deleted: {} by user: {}", resumeId, userId);
    }

    public Resume setPrimaryResume(UUID resumeId, UUID userId) {
        Resume resume = getResume(resumeId, userId);

        // Unset all other primary resumes
        List<Resume> userResumes = resumeRepository.findByUserId(userId);
        userResumes.forEach(r -> r.setIsPrimary(false));
        resumeRepository.saveAll(userResumes);

        // Set this as primary
        resume.setIsPrimary(true);
        resume = resumeRepository.save(resume);
        log.info("Resume set as primary: {} for user: {}", resumeId, userId);

        return resume;
    }

    public Resume getPrimaryResume(UUID userId) {
        return resumeRepository.findPrimaryResumeByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("No primary resume found for user"));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException("File size exceeds maximum limit of 10 MB");
        }

        String contentType = file.getContentType();
        boolean isAllowed = false;
        for (String allowedType : ALLOWED_TYPES) {
            if (allowedType.equals(contentType)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            Map<String, String> errors = new HashMap<>();
            errors.put("file", "Only PDF and DOCX files are allowed");
            throw new ValidationException("Invalid file type", errors);
        }
    }
}
