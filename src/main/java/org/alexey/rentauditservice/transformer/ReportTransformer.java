package org.alexey.rentauditservice.transformer;

import org.alexey.rentauditservice.core.dto.ReportDto;
import org.alexey.rentauditservice.core.entity.Report;

public interface ReportTransformer {

    ReportDto transformReportDtoFromEntity(Report report);
}
