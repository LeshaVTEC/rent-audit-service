package org.alexey.rentauditservice.service.impl;

import org.alexey.rentauditservice.core.entity.Audit;
import org.alexey.rentauditservice.core.entity.xml.XmlAudit;
import org.alexey.rentauditservice.core.entity.xml.XmlAuditList;
import org.alexey.rentauditservice.service.XmlFileGenerator;
import org.springframework.stereotype.Component;

import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

@Component
public class XmlFileGeneratorImpl implements XmlFileGenerator {

    private final Marshaller marshaller;

    public XmlFileGeneratorImpl(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Override
    public void generateXmlFile(List<Audit> audits, String filename) throws Exception {
        List<XmlAudit> xmlAudits = audits.stream().map(XmlAudit::from).toList();
        XmlAuditList xmlAuditList = new XmlAuditList().setAudits(xmlAudits);
        marshaller.marshal(xmlAuditList, new File(filename));
    }
}
