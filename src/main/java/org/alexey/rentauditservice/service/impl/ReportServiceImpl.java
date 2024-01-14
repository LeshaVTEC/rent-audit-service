package org.alexey.rentauditservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.alexey.rentauditservice.aop.Audited;
import org.alexey.rentauditservice.core.dto.ReportDto;
import org.alexey.rentauditservice.core.dto.UserActionAuditParamDto;
import org.alexey.rentauditservice.core.entity.Audit;
import org.alexey.rentauditservice.core.entity.Report;
import org.alexey.rentauditservice.core.entity.ReportStatus;
import org.alexey.rentauditservice.core.entity.ReportType;
import org.alexey.rentauditservice.exception.EntityNotFoundException;
import org.alexey.rentauditservice.repository.AuditRepository;
import org.alexey.rentauditservice.repository.ReportRepository;
import org.alexey.rentauditservice.service.FileGenerator;
import org.alexey.rentauditservice.service.ReportService;
import org.alexey.rentauditservice.transformer.ReportTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.alexey.rentauditservice.core.entity.AuditedAction.CREATE_REPORT;
import static org.alexey.rentauditservice.core.entity.AuditedAction.INFO_ABOUT_ACCESS_REPORT;
import static org.alexey.rentauditservice.core.entity.AuditedAction.INFO_ABOUT_ALL_REPORTS;
import static org.alexey.rentauditservice.core.entity.AuditedAction.SAVE_REPORT;
import static org.alexey.rentauditservice.core.entity.EssenceType.REPORT;
import static org.alexey.rentauditservice.core.entity.ReportStatus.DONE;
import static org.alexey.rentauditservice.core.entity.ReportStatus.ERROR;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private static final String FILE_DIRECTORY = ".";

    private final ReportRepository reportRepository;
    private final AuditRepository auditRepository;
    private final ReportTransformer reportTransformer;
    private final FileGenerator fileGenerator;

    public ReportServiceImpl(
            ReportRepository reportRepository,
            AuditRepository auditRepository,
            ReportTransformer reportTransformer,
            @Qualifier("excel-file-generator") FileGenerator fileGenerator
    ) {
        this.reportRepository = reportRepository;
        this.auditRepository = auditRepository;
        this.reportTransformer = reportTransformer;
        this.fileGenerator = fileGenerator;
    }

    @Override
    @Async
    @Audited(auditedAction = CREATE_REPORT, essenceType = REPORT)
    public void createReport(ReportType type, UserActionAuditParamDto paramDto) {
        if (!reportRepository.existsByUserId(paramDto.getUserId())){
            throw new EntityNotFoundException("user", UUID.fromString(paramDto.getUserId()));
        }
        Report reportEntityForSave = UserActionAuditParamDto.toEntity(type, paramDto);
        Report savedReport = reportRepository.saveAndFlush(reportEntityForSave);

        List<Audit> audits = auditRepository.findAllByParam(
                UUID.fromString(paramDto.getUserId()),
                LocalDateTime.of(paramDto.getFrom(), LocalTime.of(0,0)),
                LocalDateTime.of(paramDto.getTo(), LocalTime.of(0,0))
        );

        try {
            fileGenerator.generateFile(audits, savedReport.getId().toString());
            savedReport.setStatus(DONE);
        } catch (Exception exception) {
            log.error("Error while generating report" + exception);
            savedReport.setStatus(ERROR);
        }
        reportRepository.save(savedReport);
        log.info("Saved report with status " + savedReport.getStatus());
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ALL_REPORTS, essenceType = REPORT)
    public Page<ReportDto> getAllReports(Pageable pageable) {
        Page<Report> pageEntity = reportRepository.findAll(pageable);
        List<ReportDto> reportDtoList = pageEntity.stream()
                .map(reportTransformer::transformReportDtoFromEntity)
                .toList();
        return new PageImpl<ReportDto>(reportDtoList, pageable, pageEntity.getTotalElements());
    }

    @Override
    @Audited(auditedAction = SAVE_REPORT, essenceType = REPORT)
    public ResponseEntity<String> saveFileByName(String uuid) throws IOException {
        String fileName = uuid + ".xlsx";
        Path filePath = Paths.get(FILE_DIRECTORY).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            byte[] fileContent = Files.readAllBytes(filePath);
            String base64Encoded = Base64.getEncoder().encodeToString(fileContent);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.add(
                    HttpHeaders.CONTENT_TYPE,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(base64Encoded);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ACCESS_REPORT, essenceType = REPORT)
    public ReportStatus getStatusById(String id) {
        try {
            ReportStatus status = ReportStatus.valueOf(reportRepository.getStatusById(UUID.fromString(id)));
            return status;
        } catch (Exception exception) {
            log.info(exception.getMessage());
           throw new EntityNotFoundException("report", UUID.fromString(id));
            }
    }
}
