package org.alexey.rentauditservice.transformer;

import org.alexey.rentauditservice.core.dto.AuditInfoDto;
import org.alexey.rentauditservice.core.dto.PageOfAuditInfoDto;
import org.alexey.rentauditservice.core.dto.PageOfReportDto;
import org.alexey.rentauditservice.core.dto.ReportDto;
import org.springframework.data.domain.Page;

public interface PageTransformer {

    PageOfAuditInfoDto transformPageOfAuditInfoDtoFromPage(Page<AuditInfoDto> page);

    PageOfReportDto transformPageOfReportDtoFromPage(Page<ReportDto> page);

}
