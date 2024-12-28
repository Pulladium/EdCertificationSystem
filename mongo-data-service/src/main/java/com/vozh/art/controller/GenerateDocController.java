package com.vozh.art.controller;

import com.vozh.art.dto.ParticipantKey;
import com.vozh.art.dto.SignedDocRefResponse;
import com.vozh.art.dto.request.ParticipantTemplateRequest;
//import com.vozh.art.service.CertGenerator;
import com.vozh.art.service.CertGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/data/documents-generate")
@RequiredArgsConstructor
@Slf4j
public class GenerateDocController {

    private final CertGenerator certificateGenerator;

    @PostMapping
    public void generateDocument(
            @RequestBody ParticipantTemplateRequest participantData
    ) {
        log.info("Document generation started");
    }

    @PostMapping("/generate_and_save")
    public ResponseEntity<SignedDocRefResponse> generateAndSaveDocument(
            @RequestBody ParticipantTemplateRequest participantData
    ) {
        log.info("Document generation and saving started");
        try {
            SignedDocRefResponse signedDocRefResponse = certificateGenerator.generateAndSaveCertificate(participantData.getParticipantKey(), participantData.getTemplateName());
            return ResponseEntity.ok(signedDocRefResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateCertificate(@RequestBody ParticipantKey participantKey) {
        try {
//            byte[] pdfBytes = certificateGenerator.generatePdfFromHtml("sdfsd");



            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("certificate-" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ".pdf")
                            .build());

//            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
