package com.vozh.art.controller;

import com.vozh.art.dto.Participant;
import com.vozh.art.dto.request.ParticipantTemplateRequest;
//import com.vozh.art.service.CertGenerator;
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

//    private final CertGenerator certificateGenerator;

    @PostMapping
    public void generateDocument(
            @RequestBody ParticipantTemplateRequest participantData
    ) {
        log.info("Document generation started");
    }
//
//    @PostMapping("/generate")
//    public ResponseEntity<byte[]> generateCertificate(@RequestBody Participant participant) {
//        try {
//            byte[] pdfBytes = certificateGenerator.generateCertificate(participant);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDisposition(
//                    ContentDisposition.builder("attachment")
//                            .filename("certificate-" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ".pdf")
//                            .build());
//
//            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
