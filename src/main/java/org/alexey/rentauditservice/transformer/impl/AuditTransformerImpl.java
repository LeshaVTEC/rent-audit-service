package org.alexey.rentauditservice.transformer.impl;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.dto.AuditInfoDto;
import org.alexey.rentauditservice.core.dto.UserAuditDto;
import org.alexey.rentauditservice.core.entity.Audit;
import org.alexey.rentauditservice.core.entity.AuditedAction;
import org.alexey.rentauditservice.transformer.AuditTransformer;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class AuditTransformerImpl implements AuditTransformer {

    @Override
    public AuditInfoDto transformAuditInfoDtoFromEntity(Audit audit) {
        return new AuditInfoDto().setId(audit.getId())
                .setCreationDate(audit.getActionDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setUserAuditDto(getUserDtoFromAudit(audit))
                .setAction(audit.getAction().getDescription())
                .setEssenceType(audit.getEssenceType())
                .setEssenceTypeId(audit.getEssenceTypeId())
                ;
    }

    @Override
    public AuditDto transformAuditDtoFromEntity(Audit audit) {
        return new AuditDto().setId(audit.getId())
                .setCreationDate(audit.getActionDate())
                .setUserAuditDto(getUserDtoFromAudit(audit))
                .setAction(audit.getAction())
                .setEssenceType(audit.getEssenceType())
                .setEssenceTypeId(audit.getEssenceTypeId())
                ;
    }

    @Override
    public Audit transformEntityFromAuditDto(AuditDto auditDto) {
        return new Audit().setId(auditDto.getId())
                .setActionDate(auditDto.getCreationDate())
                .setUserId(auditDto.getUserAuditDto().getUserId())
                .setUserEmail(auditDto.getUserAuditDto().getEmail())
                .setUserFio(auditDto.getUserAuditDto().getFio())
                .setUserRole(auditDto.getUserAuditDto().getUserRole())
                .setAction(auditDto.getAction())
                .setEssenceType(auditDto.getEssenceType())
                .setEssenceTypeId(auditDto.getEssenceTypeId());
    }

    private UserAuditDto getUserDtoFromAudit(Audit audit) {
        return new UserAuditDto().setUserId(audit.getUserId())
                .setEmail(audit.getUserEmail())
                .setFio(audit.getUserFio())
                .setUserRole(audit.getUserRole());
    }
}
