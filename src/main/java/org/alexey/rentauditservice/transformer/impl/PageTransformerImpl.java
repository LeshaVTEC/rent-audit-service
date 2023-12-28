package org.alexey.rentauditservice.transformer.impl;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.dto.PageOfAuditDto;
import org.alexey.rentauditservice.core.dto.PageOfReportDto;
import org.alexey.rentauditservice.core.dto.ReportDto;
import org.alexey.rentauditservice.transformer.PageTransformer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageTransformerImpl implements PageTransformer {

    @Override
    public PageOfAuditDto transformPageOfAuditDtoFromPage(Page<AuditDto> page) {
        return (PageOfAuditDto) new PageOfAuditDto()
                .setContent(page.getContent())
                .setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalElements(page.getTotalElements())
                .setFirst(page.isFirst())
                .setNumberOfElements(page.getNumberOfElements())
                .setLast(page.isLast());
    }

    @Override
    public PageOfReportDto transformPageOfReportDtoFromPage(Page<ReportDto> page) {
        return (PageOfReportDto) new PageOfReportDto()
                .setContent(page.getContent())
                .setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalElements(page.getTotalElements())
                .setFirst(page.isFirst())
                .setNumberOfElements(page.getNumberOfElements())
                .setLast(page.isLast());
    }
}
