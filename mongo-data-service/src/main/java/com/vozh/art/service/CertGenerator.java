//package com.vozh.art.service;
//
//import com.lowagie.text.DocumentException;
//import com.vozh.art.dto.Participant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@Service
//public class CertGenerator {
//
//    // Using Flying Saucer
//    @Autowired
//    private TemplateEngine templateEngine;  // Thymeleaf engine
//
//    public byte[] generateCertificate(Participant participant) throws IOException, DocumentException {
//        // 1. Load template
//        Context context = new Context();
//        context.setVariable("name", participant.getName() + " " + participant.getSurname());
//        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
//
//        // 2. Process template
//        String processedHtml = templateEngine.process("certificate-template", context);
//
//        // 3. Convert to PDF
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(processedHtml);
//        renderer.layout();
//        renderer.createPDF(outputStream);
//
//        return outputStream.toByteArray();
//    }
//}
