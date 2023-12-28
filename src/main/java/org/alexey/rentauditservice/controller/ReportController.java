package org.alexey.rentauditservice.controller;

import org.alexey.rentauditservice.core.dto.PageOfReportDto;
import org.alexey.rentauditservice.core.dto.UserActionAuditParamDto;
import org.alexey.rentauditservice.core.entity.ReportStatus;
import org.alexey.rentauditservice.core.entity.ReportType;
import org.alexey.rentauditservice.service.ReportService;
import org.alexey.rentauditservice.transformer.PageTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    private final ReportService reportService;
    private final PageTransformer pageTransformer;

    public ReportController(ReportService reportService, PageTransformer pageTransformer) {
        this.reportService = reportService;
        this.pageTransformer = pageTransformer;
    }

    @PostMapping("/{type}")
    public String startReport(@PathVariable ReportType type, @RequestBody UserActionAuditParamDto paramDto) {
        reportService.createReport(type, paramDto);
        return "Report started";
    }

    @GetMapping
    public PageOfReportDto getListReports(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return pageTransformer.transformPageOfReportDtoFromPage(reportService.getAllReports(pageable));
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/{id}")
    public ReportStatus getReportStatus(@PathVariable String id) {
        return reportService.getStatusById(id);
    }
}
