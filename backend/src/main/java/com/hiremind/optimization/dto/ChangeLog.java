package com.hiremind.optimization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLog {

    @JsonProperty("change_type")
    private String changeType; // ADDED, REMOVED, MODIFIED, REFORMATTED

    @JsonProperty("section")
    private String section; // Skills, Experience, etc.

    @JsonProperty("original_text")
    private String originalText;

    @JsonProperty("new_text")
    private String newText;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("impact")
    private String impact; // Description of how this change impacts the resume
}
