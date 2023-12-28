package org.alexey.rentauditservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.alexey.rentauditservice.core.entity.ReportStatus;
import org.alexey.rentauditservice.core.entity.ReportType;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ReportDto {

    private UUID id;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private ReportStatus status;

    private ReportType type;

    private String description;

    private UserActionAuditParamDto params;
}
