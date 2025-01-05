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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/data-mongo/documents-generate")
@RequiredArgsConstructor
@Slf4j
public class GenerateDocController {

    private final CertGenerator certificateGenerator;





///api/data/documents-generate/generate_and_save
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

    @PostMapping("/massive_generate_and_save")
    public ResponseEntity<List<SignedDocRefResponse>> generateAndSaveDocuments(
            @RequestBody List<ParticipantTemplateRequest> participantData
    ) {
        log.info("Massive document generation and saving started");
        List<SignedDocRefResponse> signedDocRefResponses = new ArrayList<>();
        for (ParticipantTemplateRequest participantTemplateRequest : participantData) {
            try {
                SignedDocRefResponse signedDocRefResponse = certificateGenerator.generateAndSaveCertificate(participantTemplateRequest.getParticipantKey(), participantTemplateRequest.getTemplateName());
                signedDocRefResponses.add(signedDocRefResponse);
                log.trace("Document generated and saved: {}", signedDocRefResponse);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return ResponseEntity.ok(signedDocRefResponses);
    }
}
