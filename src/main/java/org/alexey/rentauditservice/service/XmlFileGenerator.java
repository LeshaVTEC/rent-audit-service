package org.alexey.rentauditservice.service;

import org.alexey.rentauditservice.core.entity.Audit;

import java.util.List;

public interface XmlFileGenerator {

    void generateXmlFile(List<Audit> audits, String filename) throws Exception;
}
