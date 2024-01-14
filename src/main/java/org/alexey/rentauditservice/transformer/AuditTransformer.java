package org.alexey.rentauditservice.transformer;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.dto.AuditInfoDto;
import org.alexey.rentauditservice.core.entity.Audit;

public interface AuditTransformer {

    AuditInfoDto transformAuditInfoDtoFromEntity(Audit audit);

    AuditDto transformAuditDtoFromEntity(Audit audit);

    Audit transformEntityFromAuditDto(AuditDto auditDto);
}
