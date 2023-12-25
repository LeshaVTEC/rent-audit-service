package org.alexey.rentauditservice.service;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuditService {

    Page<AuditDto> getAllAudits(Pageable pageable);

    AuditDto findAuditById(UUID id);

    AuditDto saveAction(AuditDto auditDto);
}
