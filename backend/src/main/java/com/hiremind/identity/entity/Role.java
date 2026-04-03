package com.hiremind.identity.entity;

public enum Role {
    ADMIN("Administrator with full system access"),
    RECRUITER("Recruiter can manage job descriptions and analyze candidates"),
    CANDIDATE("Candidate can upload resume and receive optimization suggestions");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
