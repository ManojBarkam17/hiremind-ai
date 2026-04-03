package com.hiremind.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillExtractorService {

    private final ObjectMapper objectMapper;

    // Comprehensive skill database organized by category
    private static final Map<String, Set<String>> SKILL_DATABASE = new HashMap<>();

    static {
        // Programming Languages
        SKILL_DATABASE.put("Programming", new HashSet<>(Arrays.asList(
            "Java", "Python", "JavaScript", "C++", "C#", "Go", "Rust", "PHP", "Ruby", "Swift",
            "Kotlin", "TypeScript", "Scala", "R", "MATLAB", "Perl", "Objective-C", "VB.NET"
        )));

        // Web Frameworks
        SKILL_DATABASE.put("Web Frameworks", new HashSet<>(Arrays.asList(
            "Spring Boot", "Spring Framework", "React", "Angular", "Vue.js", "Django", "Flask",
            "Express.js", "ASP.NET Core", "Laravel", "Rails", "FastAPI", "Next.js", "Nuxt.js"
        )));

        // Databases
        SKILL_DATABASE.put("Databases", new HashSet<>(Arrays.asList(
            "PostgreSQL", "MySQL", "MongoDB", "Redis", "Cassandra", "Elasticsearch",
            "Oracle", "SQL Server", "DynamoDB", "Neo4j", "CouchDB", "Firebase"
        )));

        // Cloud Platforms
        SKILL_DATABASE.put("Cloud", new HashSet<>(Arrays.asList(
            "AWS", "Azure", "Google Cloud", "Kubernetes", "Docker", "OpenStack", "Heroku"
        )));

        // DevOps & Tools
        SKILL_DATABASE.put("DevOps", new HashSet<>(Arrays.asList(
            "Docker", "Kubernetes", "Jenkins", "GitLab CI/CD", "GitHub Actions", "Terraform",
            "Ansible", "Git", "Maven", "Gradle", "Helm", "Prometheus", "ELK Stack"
        )));

        // Data Technologies
        SKILL_DATABASE.put("Data", new HashSet<>(Arrays.asList(
            "Spark", "Hadoop", "Kafka", "Airflow", "ETL", "Data Warehouse", "BigQuery",
            "Snowflake", "Tableau", "Power BI", "Looker", "dbt"
        )));

        // Other Technologies
        SKILL_DATABASE.put("Other", new HashSet<>(Arrays.asList(
            "REST API", "GraphQL", "Microservices", "Machine Learning", "Deep Learning",
            "NLP", "Agile", "Scrum", "JIRA", "Confluence", "Linux", "Windows", "macOS",
            "HTML", "CSS", "SQL", "JSON", "XML", "YAML"
        )));
    }

    public Map<String, Object> extractSkills(String resumeText) {
        Map<String, Object> result = new HashMap<>();
        Map<String, List<String>> extractedSkills = new HashMap<>();
        Set<String> allFoundSkills = new HashSet<>();

        String lowerText = resumeText.toLowerCase();

        // Extract skills by category
        for (Map.Entry<String, Set<String>> entry : SKILL_DATABASE.entrySet()) {
            List<String> found = new ArrayList<>();
            for (String skill : entry.getValue()) {
                if (skillFoundInText(lowerText, skill.toLowerCase())) {
                    found.add(skill);
                    allFoundSkills.add(skill);
                }
            }
            if (!found.isEmpty()) {
                extractedSkills.put(entry.getKey(), found);
            }
        }

        result.put("skills_by_category", extractedSkills);
        result.put("total_skills_found", allFoundSkills.size());
        result.put("all_skills", new ArrayList<>(allFoundSkills));
        result.put("confidence", calculateSkillConfidence(resumeText, allFoundSkills));

        return result;
    }

    private boolean skillFoundInText(String text, String skill) {
        // Word boundary matching to avoid partial matches
        String pattern = "\\b" + Pattern.quote(skill) + "\\b";
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text).find();
    }

    private double calculateSkillConfidence(String resumeText, Set<String> skills) {
        if (skills.isEmpty()) {
            return 0.0;
        }

        // Calculate based on frequency and distribution
        int matches = 0;
        int totalSkillOccurrences = 0;

        for (String skill : skills) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(skill) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(resumeText);
            while (matcher.find()) {
                totalSkillOccurrences++;
            }
            matches++;
        }

        // Confidence: based on number of skills found and frequency
        double frequency = totalSkillOccurrences / (double) Math.max(1, resumeText.split("\\s+").length);
        double skillDiversity = matches / (double) SKILL_DATABASE.values().stream()
            .mapToInt(Set::size)
            .sum();

        return Math.min(1.0, (skillDiversity * 0.6) + (Math.min(frequency * 10, 1.0) * 0.4));
    }

    public Set<String> extractKeywords(String text) {
        Set<String> keywords = new HashSet<>();
        String[] words = text.toLowerCase()
            .replaceAll("[^a-z0-9\\s+#.-]", " ")
            .split("\\s+");

        for (String word : words) {
            if (word.length() > 3 && !isCommonWord(word)) {
                keywords.add(word);
            }
        }

        return keywords;
    }

    private boolean isCommonWord(String word) {
        Set<String> commonWords = new HashSet<>(Arrays.asList(
            "the", "and", "for", "with", "from", "that", "this", "which", "have",
            "they", "been", "their", "more", "than", "some", "such", "these", "other"
        ));
        return commonWords.contains(word);
    }
}
