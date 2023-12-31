package org.alexey.rentauditservice.repository;

import org.alexey.rentauditservice.core.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    String getStatusById(UUID id);
}
