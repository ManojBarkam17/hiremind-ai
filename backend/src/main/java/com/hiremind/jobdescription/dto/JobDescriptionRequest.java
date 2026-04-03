package com.hiremind.jobdescription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDescriptionRequest {

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 255, message = "Job title must be between 3 and 255 characters")
    private String jobTitle;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 500, message = "Company name must be between 2 and 500 characters")
    private String companyName;

    private String description;

    private String requirements;

    private String preferences;

    private String responsibilities;

    private String location;

    private String employmentType;

    private String salaryRange;
}
