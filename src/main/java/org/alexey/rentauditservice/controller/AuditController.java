package org.alexey.rentauditservice.controller;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.dto.PageOfAuditDto;
import org.alexey.rentauditservice.service.AuditService;
import org.alexey.rentauditservice.transformer.PageTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/audit")
public class AuditController {

    private final AuditService auditService;
    private final PageTransformer pageTransformer;

    public AuditController(AuditService auditService, PageTransformer pageTransformer) {
        this.auditService = auditService;
        this.pageTransformer = pageTransformer;
    }

    @GetMapping
    public PageOfAuditDto getListAudits(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return pageTransformer.transformPageOfAuditDtoFromPage(auditService.getAllAudits(pageable));
    }

    @GetMapping("/{id}")
    public AuditDto getAuditById(@PathVariable UUID id) {
        return auditService.findAuditById(id);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    AuditDto acceptRequestToCreateLog(@RequestHeader String AUTHORIZATION, @RequestBody AuditDto auditDto) {
        return auditService.saveAction(auditDto);
    }
}
