package org.alexey.rentauditservice.service;

import org.alexey.rentauditservice.core.entity.Audit;

import java.util.List;

public interface FileGenerator {

    void generateFile(List<Audit> audits, String filename) throws Exception;
}
