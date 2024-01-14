package org.alexey.rentauditservice.service;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.dto.AuditInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuditService {

    Page<AuditInfoDto> getAllAudits(Pageable pageable);

    AuditInfoDto findAuditById(UUID id);

    AuditDto saveAction(AuditDto auditDto);
}
