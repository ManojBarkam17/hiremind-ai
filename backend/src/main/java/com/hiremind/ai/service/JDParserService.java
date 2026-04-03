package com.hiremind.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class JDParserService {

    private final SkillExtractorService skillExtractorService;
    private final ObjectMapper objectMapper;

    public Map<String, Object> parseJobDescription(String jdText) {
        Map<String, Object> parsed = new HashMap<>();

        parsed.put("required_skills", extractRequiredSkills(jdText));
        parsed.put("preferred_skills", extractPreferredSkills(jdText));
        parsed.put("experience_level", extractExperienceLevel(jdText));
        parsed.put("years_of_experience", extractYearsOfExperience(jdText));
        parsed.put("education_requirements", extractEducationRequirements(jdText));
        parsed.put("responsibilities", extractResponsibilities(jdText));
        parsed.put("keywords", skillExtractorService.extractKeywords(jdText));

        return parsed;
    }

    private List<String> extractRequiredSkills(String jdText) {
        List<String> skills = new ArrayList<>();
        String lowerText = jdText.toLowerCase();

        // Skills in "Required" or "Must have" sections
        String[] patterns = {
            "required:([^\\n]+?)(?=\\n|$)",
            "must have:([^\\n]+?)(?=\\n|$)",
            "required skills?:([^\\n]+?)(?=\\n|$)",
            "essential skills?:([^\\n]+?)(?=\\n|$)"
        };

        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            var matcher = p.matcher(jdText);
            while (matcher.find()) {
                String match = matcher.group(1);
                skills.addAll(parseSkillsFromString(match));
            }
        }

        // If no specific section found, extract from all text
        if (skills.isEmpty()) {
            Map<String, Object> allSkills = skillExtractorService.extractSkills(jdText);
            skills.addAll((Collection<? extends String>) allSkills.getOrDefault("all_skills", new ArrayList<>()));
        }

        return skills.stream().distinct().toList();
    }

    private List<String> extractPreferredSkills(String jdText) {
        List<String> skills = new ArrayList<>();
        String lowerText = jdText.toLowerCase();

        String[] patterns = {
            "preferred:([^\\n]+?)(?=\\n|$)",
            "nice to have:([^\\n]+?)(?=\\n|$)",
            "preferred skills?:([^\\n]+?)(?=\\n|$)",
            "additional:([^\\n]+?)(?=\\n|$)"
        };

        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            var matcher = p.matcher(jdText);
            while (matcher.find()) {
                String match = matcher.group(1);
                skills.addAll(parseSkillsFromString(match));
            }
        }

        return skills.stream().distinct().toList();
    }

    private String extractExperienceLevel(String jdText) {
        String lowerText = jdText.toLowerCase();

        if (lowerText.contains("senior") || lowerText.contains("lead") || lowerText.contains("principal")) {
            return "SENIOR";
        } else if (lowerText.contains("mid-level") || lowerText.contains("intermediate")) {
            return "MID_LEVEL";
        } else if (lowerText.contains("junior") || lowerText.contains("entry") || lowerText.contains("entry-level")) {
            return "JUNIOR";
        }

        return "UNSPECIFIED";
    }

    private String extractYearsOfExperience(String jdText) {
        Pattern pattern = Pattern.compile("(\\d+)\\+?\\s*(?:years?|yrs?)", Pattern.CASE_INSENSITIVE);
        var matcher = pattern.matcher(jdText);

        if (matcher.find()) {
            return matcher.group(1) + "+ years";
        }

        return "Not specified";
    }

    private List<String> extractEducationRequirements(String jdText) {
        List<String> education = new ArrayList<>();
        String lowerText = jdText.toLowerCase();

        if (lowerText.contains("bachelor") || lowerText.contains("b.s.") || lowerText.contains("b.a.")) {
            education.add("Bachelor's Degree");
        }
        if (lowerText.contains("master") || lowerText.contains("m.s.") || lowerText.contains("m.a.")) {
            education.add("Master's Degree");
        }
        if (lowerText.contains("phd") || lowerText.contains("ph.d.")) {
            education.add("Ph.D.");
        }
        if (lowerText.contains("high school") || lowerText.contains("hs diploma")) {
            education.add("High School Diploma");
        }
        if (lowerText.contains("certification") || lowerText.contains("certified")) {
            education.add("Professional Certification");
        }

        return education;
    }

    private List<String> extractResponsibilities(String jdText) {
        List<String> responsibilities = new ArrayList<>();
        String[] lines = jdText.split("\n");

        boolean inResponsibilities = false;
        for (String line : lines) {
            String lowerLine = line.toLowerCase();

            if (lowerLine.contains("responsibilities") || lowerLine.contains("you will")) {
                inResponsibilities = true;
                continue;
            }

            if (inResponsibilities && line.trim().startsWith("-") || line.trim().startsWith("•")) {
                String resp = line.replaceAll("^[-•\\s]+", "").trim();
                if (!resp.isEmpty() && resp.length() > 10) {
                    responsibilities.add(resp);
                }
            }
        }

        return responsibilities;
    }

    private List<String> parseSkillsFromString(String skillString) {
        List<String> skills = new ArrayList<>();

        String[] parts = skillString.split("[,/]");
        for (String part : parts) {
            String skill = part.trim();
            if (!skill.isEmpty()) {
                skills.add(skill);
            }
        }

        return skills;
    }
}
