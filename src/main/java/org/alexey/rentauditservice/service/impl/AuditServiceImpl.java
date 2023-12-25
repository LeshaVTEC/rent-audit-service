package org.alexey.rentauditservice.service.impl;

import org.alexey.rentauditservice.core.dto.AuditDto;
import org.alexey.rentauditservice.core.entity.Audit;
import org.alexey.rentauditservice.exception.EntityNotFoundException;
import org.alexey.rentauditservice.repository.AuditRepository;
import org.alexey.rentauditservice.service.AuditService;
import org.alexey.rentauditservice.transformer.AuditTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditTransformer auditTransformer;

    public AuditServiceImpl(AuditRepository auditRepository, AuditTransformer auditTransformer) {
        this.auditRepository = auditRepository;
        this.auditTransformer = auditTransformer;
    }

    @Override
    public Page<AuditDto> getAllAudits(Pageable pageable) {
        Page<Audit> pageEntity = auditRepository.findAll(pageable);
        List<AuditDto> auditDtoList = pageEntity.stream()
                .map(auditTransformer::transformAuditDtoFromEntity)
                .toList();
        return new PageImpl<AuditDto>(auditDtoList, pageable, pageEntity.getTotalElements());
    }

    @Override
    public AuditDto findAuditById(UUID id) {
        Audit auditById = auditRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Audit", id));
        return auditTransformer.transformAuditDtoFromEntity(auditById);
    }

    @Override
    public AuditDto saveAction(AuditDto auditDto) {
        Audit entity = auditRepository.save(auditTransformer.transformEntityFromAuditDto(auditDto));
        return auditTransformer.transformAuditDtoFromEntity(entity);
    }
}
