package org.alexey.rentauditservice.service.impl;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.alexey.rentauditservice.core.entity.ReportStatus.DONE;
import static org.alexey.rentauditservice.core.entity.ReportStatus.ERROR;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

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
    public void createReport(ReportType type, UserActionAuditParamDto paramDto) {
        Report reportEntityForSave = UserActionAuditParamDto.toEntity(type, paramDto);
        Report savedReport = reportRepository.save(reportEntityForSave);

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
    public Page<ReportDto> getAllReports(Pageable pageable) {
        Page<Report> pageEntity = reportRepository.findAll(pageable);
        List<ReportDto> reportDtoList = pageEntity.stream()
                .map(reportTransformer::transformReportDtoFromEntity)
                .toList();
        return new PageImpl<ReportDto>(reportDtoList, pageable, pageEntity.getTotalElements());
    }

    @Override
    public String saveFileByName(String fileName) {
        String userHome = System.getProperty("user.home");
        String downloadsDirectory = userHome + File.separator + "Downloads" + File.separator;
        try {
            File file = new File(downloadsDirectory + fileName + ".xlsx");

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.copy(new File(fileName + ".xlsx").toPath(), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "Файл '" + fileName + "' успешно сохранен по пути: " + file.getAbsolutePath();
        } catch (IOException exception) {
            exception.printStackTrace();
            return "Ошибка сохранения файла: " + exception.getMessage();
        }
    }

    @Override
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
