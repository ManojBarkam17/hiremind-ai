package com.hiremind.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreBreakdown {

    @JsonProperty("required_skills_score")
    private Double requiredSkillsScore;

    @JsonProperty("preferred_skills_score")
    private Double preferredSkillsScore;

    @JsonProperty("semantic_similarity_score")
    private Double semanticSimilarityScore;

    @JsonProperty("domain_alignment_score")
    private Double domainAlignmentScore;

    @JsonProperty("ats_keywords_score")
    private Double atsKeywordsScore;

    @JsonProperty("formatting_score")
    private Double formattingScore;

    @JsonProperty("total_score")
    private Double totalScore;

    @JsonProperty("match_level")
    private String matchLevel;

    private List<String> strengths;

    private List<String> weaknesses;

    private List<String> recommendations;

    public static ScoreBreakdown fromEngine(com.hiremind.ai.engine.ScoringEngine.ScoreBreakdown engineScore) {
        return ScoreBreakdown.builder()
            .requiredSkillsScore(engineScore.getRequiredSkillsScore())
            .preferredSkillsScore(engineScore.getPreferredSkillsScore())
            .semanticSimilarityScore(engineScore.getSemanticSimilarityScore())
            .domainAlignmentScore(engineScore.getDomainAlignmentScore())
            .atsKeywordsScore(engineScore.getAtsKeywordsScore())
            .formattingScore(engineScore.getFormattingScore())
            .totalScore(engineScore.getTotalScore())
            .matchLevel(engineScore.getMatchLevel())
            .strengths(engineScore.getStrengths())
            .weaknesses(engineScore.getWeaknesses())
            .recommendations(engineScore.getRecommendations())
            .build();
    }
}
