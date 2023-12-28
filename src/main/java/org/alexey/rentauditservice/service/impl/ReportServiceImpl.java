package org.alexey.rentauditservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.alexey.rentauditservice.core.dto.ReportDto;
import org.alexey.rentauditservice.core.dto.UserActionAuditParamDto;
import org.alexey.rentauditservice.core.entity.Audit;
import org.alexey.rentauditservice.core.entity.Report;
import org.alexey.rentauditservice.core.entity.ReportStatus;
import org.alexey.rentauditservice.core.entity.ReportType;
import org.alexey.rentauditservice.repository.AuditRepository;
import org.alexey.rentauditservice.repository.ReportRepository;
import org.alexey.rentauditservice.service.ReportService;
import org.alexey.rentauditservice.service.XmlFileGenerator;
import org.alexey.rentauditservice.transformer.ReportTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.alexey.rentauditservice.core.entity.ReportStatus.ERROR;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AuditRepository auditRepository;
    private final ReportTransformer reportTransformer;
    private final XmlFileGenerator xmlFileGenerator;

    public ReportServiceImpl(
            ReportRepository reportRepository,
            AuditRepository auditRepository,
            ReportTransformer reportTransformer,
            XmlFileGenerator xmlFileGenerator
    ) {
        this.reportRepository = reportRepository;
        this.auditRepository = auditRepository;
        this.reportTransformer = reportTransformer;
        this.xmlFileGenerator = xmlFileGenerator;
    }

    @Override
    @Async
    public void createReport(ReportType type, UserActionAuditParamDto paramDto) {
        Report reportEntityForSave = UserActionAuditParamDto.toEntity(type, paramDto);
        var savedReport = reportRepository.save(reportEntityForSave);

        List<Audit> audits = auditRepository.findAllByParam(
                UUID.fromString(paramDto.getUserId()),
                LocalDateTime.of(paramDto.getFrom(), LocalTime.of(0,0)),
                LocalDateTime.of(paramDto.getTo(), LocalTime.of(0,0))
        );

        try {
            xmlFileGenerator.generateXmlFile(audits, savedReport.getXmlFileName());
        } catch (Exception exception) {
            log.error("Error while generating report" + exception);
            savedReport.setStatus(ERROR);
        }
    }

    @Override
    public Page<ReportDto> getAllReports(Pageable pageable) {
        Page<Report> pageEntity = reportRepository.findAll(pageable);
        List<ReportDto> reportDtoList = pageEntity.stream()
                .map(reportTransformer::transformReportDtoFromEntity)
                .toList();
        return new PageImpl<ReportDto>(reportDtoList, pageable, pageEntity.getTotalElements());
    }

    @Override
    public ReportStatus getStatusById(String id) {
        return ReportStatus.valueOf(reportRepository.getStatusById(UUID.fromString(id)));
    }
}
