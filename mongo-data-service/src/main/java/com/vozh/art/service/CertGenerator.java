package com.vozh.art.service;


import com.vozh.art.dto.ParticipantKey;
import com.vozh.art.dto.SignedDocRefResponse;
import com.vozh.art.entity.SignedDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;


import java.io.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertGenerator {


//maybe no need extending DocService just use it as a service
    private final DocService docService;

    public SignedDocRefResponse generateAndSaveCertificate(ParticipantKey participantKey, String templateName) throws Exception {

        //todo tample name is not used
        String htmlPath = "template/certificate.html";
        String cssPath = "template/css/certificate-style.css";

        String processedHtmlCss = parseThymeleafTemplate(htmlPath, cssPath, participantKey);

        byte[] pdfData = null;
        try {
            pdfData = generatePdfFromHtml(processedHtmlCss);
        } catch (IOException e) {
            log.error("Error while generating PDF from HTML", e);
        }

        // Save PDF to GridFS
        SignedDoc savedDocument = docService.saveDocument(pdfData, participantKey.getName(), "application/pdf");

        String uuidOfDoc= savedDocument.getId();

        return SignedDocRefResponse.builder()
                .uuidOfDoc(uuidOfDoc)
                .participantKey(participantKey)
                .build();
    }




    private String parseThymeleafTemplate(String htmlPath, String cssPath, ParticipantKey participantKey) {

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


    private byte[] generatePdfFromHtml(String ProcessedHtmlCss) throws IOException {

        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
//        OutputStream outputStream = new FileOutputStream(outputFolder);



        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(ProcessedHtmlCss);
        renderer.layout();
//        renderer.createPDF(outputStream);
        renderer.createPDF(byteOutputStream);

//        outputStream.close();
        byteOutputStream.close();

        return byteOutputStream.toByteArray();
    }



}
