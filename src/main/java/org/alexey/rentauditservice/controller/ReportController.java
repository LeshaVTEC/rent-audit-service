package org.alexey.rentauditservice.controller;

import org.alexey.rentauditservice.core.dto.PageOfReportDto;
import org.alexey.rentauditservice.core.dto.UserActionAuditParamDto;
import org.alexey.rentauditservice.core.entity.ReportType;
import org.alexey.rentauditservice.service.ReportService;
import org.alexey.rentauditservice.transformer.PageTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    private final ReportService reportService;
    private final PageTransformer pageTransformer;

    public ReportController(ReportService reportService, PageTransformer pageTransformer) {
        this.reportService = reportService;
        this.pageTransformer = pageTransformer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{type}")
    public void startReport(@PathVariable ReportType type,
                            @Validated @RequestBody UserActionAuditParamDto paramDto) {
        reportService.createReport(type, paramDto);
    }

    @GetMapping
    public PageOfReportDto getListReports(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return pageTransformer.transformPageOfReportDtoFromPage(reportService.getAllReports(pageable));
    }

    @GetMapping(value = "/{UUID}/export")
    public ResponseEntity<String> saveFile(@PathVariable (name = "UUID") String uuid) throws IOException{
        return reportService.saveFileByName(uuid);
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/{uuid}/export")
    public ResponseEntity getReportStatus(@PathVariable String uuid) {
        return switch (reportService.getStatusById(uuid)) {
            case DONE -> ResponseEntity.status(200).build();
            case ERROR, LOADED, PROGRESS -> ResponseEntity.status(505).build();
        };
    }
}
