package org.alexey.rentauditservice.service;

import org.alexey.rentauditservice.core.dto.ReportDto;
import org.alexey.rentauditservice.core.dto.UserActionAuditParamDto;
import org.alexey.rentauditservice.core.entity.ReportStatus;
import org.alexey.rentauditservice.core.entity.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    void createReport(ReportType type, UserActionAuditParamDto paramDto);

    Page<ReportDto> getAllReports(Pageable pageable);

    String saveFileByName(String fileName);

    ReportStatus getStatusById(String id);
}
