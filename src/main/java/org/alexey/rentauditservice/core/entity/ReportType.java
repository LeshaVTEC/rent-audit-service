package org.alexey.rentauditservice.core.entity;

public enum ReportType {

    JOURNAL_AUDIT("Journal Audit");

    private String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
