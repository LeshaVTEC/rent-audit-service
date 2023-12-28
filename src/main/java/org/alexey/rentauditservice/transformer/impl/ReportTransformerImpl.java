package org.alexey.rentauditservice.transformer.impl;

import org.alexey.rentauditservice.core.dto.ReportDto;
import org.alexey.rentauditservice.core.dto.UserActionAuditParamDto;
import org.alexey.rentauditservice.core.entity.Report;
import org.alexey.rentauditservice.transformer.ReportTransformer;
import org.springframework.stereotype.Component;

@Component
public class ReportTransformerImpl implements ReportTransformer {

    @Override
    public ReportDto transformReportDtoFromEntity(Report report) {
        return new ReportDto().setStatus(report.getStatus())
                .setType(report.getType())
                .setDescription(report.getDescription())
                .setParams(getParamFromEntity(report))
                .setId(report.getId())
                .setCreationDate(report.getCreationDate())
                .setUpdateDate(report.getUpdateDate());
    }

    private UserActionAuditParamDto getParamFromEntity(Report report) {
        return new UserActionAuditParamDto().setUserId(report.getUserId())
                .setFrom(report.getFromDate())
                .setTo(report.getToDate());
    }
}
