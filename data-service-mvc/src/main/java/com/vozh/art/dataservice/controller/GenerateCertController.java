package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.SingedDocRef;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import com.vozh.art.dataservice.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/data/certificates-gen")
@RequiredArgsConstructor
@Slf4j
public class GenerateCertController {

    private final RestTemplate restTemplate;
    private final CertificateService certificateService;

    @PostMapping("/generate")
    public ResponseEntity<SingedDocRef> generateCertificate(@RequestBody String certId){
        log.info("Certificate generation started");

        validateCertificate(certId);

        Certificate currCert = certificateService.getById(Long.valueOf(certId));

        AtomicReference<ParticipantKey> participantKey = new AtomicReference<>();

        currCert.getCertificateParticipants().forEach(certParticipants -> {
            participantKey.set(certParticipants.getParticipant().getParticipantKey());
        });


        SingedDocRef signedDocRefRequest = new SingedDocRef();
        signedDocRefRequest.setParticipantKey(participantKey.get());
        log.info("ParticipantKey: {}", participantKey.get());


        SingedDocRef signedDocResult = restTemplate.postForObject("http://mongo-service//api/data/documents-generate/generate_and_save", signedDocRefRequest, SingedDocRef.class);


        log.info("Document generation finished, result: {}", signedDocResult);


        return ResponseEntity.ok(signedDocResult);

    }




    private void validateCertificate(String certId){
        Certificate currCert = null;
        try {
            currCert = certificateService.getById(Long.valueOf(certId));
        }
        catch (Exception e){
            throw new RuntimeException("Certificate with id: " + certId + " not found");
        }
        if(currCert == null){
            throw new RuntimeException("Certificate with id: " + certId + " not found");
        }

        //must-have validation for pdf generation
        validateCertParticipant(currCert);


    }

    private void validateCertParticipant(Certificate currCert){
        if(currCert.getCertificateParticipants() == null){
            throw new NullPointerException("Set <Certificate participants> is null");
        }
        if(currCert.getCertificateParticipants().isEmpty()){
            throw new RuntimeException("Cert have to have at least one participant, Set <Certificate participants> is empty");
        }

    }
    private void validateCertCategory(Certificate currCert){
        if(currCert.getCategories() == null){
            throw new NullPointerException("Set <Certificate categories> is null");
        }
        if(currCert.getCategories().isEmpty()){
            throw new RuntimeException("Cert have to have at least one category, Set <Certificate categories> is empty");
        }
    }

    private void validateCertIssuers(Certificate currCert){
        if(currCert.getIssuers() == null){
            throw new NullPointerException("Set <Certificate issuers> is null");
        }
        if(currCert.getIssuers().isEmpty()){
            throw new RuntimeException("Cert have to have at least one issuer, Set <Certificate issuers> is empty");
        }
    }
}