package com.vozh.art.service;


import com.vozh.art.dto.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CertGenerator {


    public String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);


        // Set up template resolver for CSS
        ClassLoaderTemplateResolver cssResolver = new ClassLoaderTemplateResolver();
        cssResolver.setSuffix(".css");
        cssResolver.setTemplateMode(TemplateMode.CSS);
//        cssResolver.setCharacterEncoding("UTF-8");


        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Process CSS template
        Context cssContext = new Context();
        cssContext.setVariable("backgroundColor", "#009688"); // Example variable for CSS
        String processedCss = templateEngine.process("template/css/certificate-style.css", cssContext);




        Context htmlContext = new Context();
        htmlContext.setVariable("title", "Certificate of Achievement");
        htmlContext.setVariable("name", "John Doe");
        htmlContext.setVariable("description", "Has successfully completed the course");
        htmlContext.setVariable("dateTime", "December 28, 2024");


        htmlContext.setVariable("styles", processedCss);


        return templateEngine.process("template/certificate.html", htmlContext);
    }


    public void generatePdfFromHtml(String html) throws IOException {

        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";


        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }
}
