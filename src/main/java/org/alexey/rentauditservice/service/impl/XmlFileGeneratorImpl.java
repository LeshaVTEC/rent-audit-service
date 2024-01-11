package org.alexey.rentauditservice.service.impl;

import org.alexey.rentauditservice.core.entity.Audit;
import org.alexey.rentauditservice.core.entity.xml.XmlAudit;
import org.alexey.rentauditservice.core.entity.xml.XmlAuditList;
import org.alexey.rentauditservice.service.FileGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

@Qualifier("xml-file-generator")
@Component
public class XmlFileGeneratorImpl implements FileGenerator {

    private final Marshaller marshaller;

    public XmlFileGeneratorImpl(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Override
    public void generateFile(List<Audit> audits, String filename) throws Exception {
        List<XmlAudit> xmlAudits = audits.stream().map(XmlAudit::from).toList();
        XmlAuditList xmlAuditList = new XmlAuditList().setAudits(xmlAudits);
        marshaller.marshal(xmlAuditList, new File(filename + ".xml"));
    }
}
