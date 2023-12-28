package org.alexey.rentauditservice.transformer;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.dto.PageOfAuditDto;
import org.alexey.rentauditservice.core.dto.PageOfReportDto;
import org.alexey.rentauditservice.core.dto.ReportDto;
import org.springframework.data.domain.Page;

public interface PageTransformer {

    PageOfAuditDto transformPageOfAuditDtoFromPage(Page<AuditDto> page);

    PageOfReportDto transformPageOfReportDtoFromPage(Page<ReportDto> page);
}
