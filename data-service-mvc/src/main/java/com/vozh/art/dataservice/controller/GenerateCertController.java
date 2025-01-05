package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.SingedDocRef;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import com.vozh.art.dataservice.service.CertificateService;
import com.vozh.art.dataservice.service.SignedDocRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/data/certificates-gen")
@RequiredArgsConstructor
@Slf4j
public class GenerateCertController {

    private final RestTemplate restTemplate;
    private final CertificateService certificateService;
    private final SignedDocRefService signedDocRefService;

    @PostMapping("/generateLastAdded")
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

        if(signedDocResult == null){
            throw new RuntimeException("Document generation failed null response from mongo-service");
        }

        log.info("Document generation finished, result: {}", signedDocResult);
        signedDocResult = signedDocRefService.save(signedDocResult);

        if (currCert.getSignedDocumentsUUIDs() == null) {
            currCert.setSignedDocumentsUUIDs(new HashSet<>());
        }
        currCert.getSignedDocumentsUUIDs().add(signedDocResult);

        certificateService.save(currCert);


        return ResponseEntity.ok(signedDocResult);

    }



    @PostMapping("/massiveDocsForCert")
    public ResponseEntity<List<SingedDocRef>> generateDocsForCertificate(@RequestBody String certId){
        log.info("Certificate generation started");

        validateCertificate(certId);

        Certificate currCert = certificateService.getById(Long.valueOf(certId));

        List<ParticipantKey> participantKeys = currCert.getCertificateParticipants().stream()
                .map(certParticipants -> certParticipants.getParticipant().getParticipantKey())
                .toList();


        List<SingedDocRef> signedDocsRefRequest = participantKeys.stream()
                .map(participantKey -> {
                    SingedDocRef signedDocRefRequest = new SingedDocRef();
                    signedDocRefRequest.setParticipantKey(participantKey);
                    return signedDocRefRequest;
                })
                .toList();
//        SingedDocRef signedDocRefRequest = new SingedDocRef();
//        signedDocRefRequest.setParticipantKey(participantKey.get());
//        log.info("ParticipantKey: {}", participantKey.get());


        List<SingedDocRef> signedDocResults = restTemplate.postForObject("http://mongo-service//api/data/documents-generate/massive_generate_and_save", signedDocsRefRequest, List.class);


        if(signedDocResults == null){
            throw new RuntimeException("Massive Document generation failed null response from mongo-service");
        }

        signedDocResults = signedDocRefService.saveAll(signedDocResults);

        if (currCert.getSignedDocumentsUUIDs() == null) {
            currCert.setSignedDocumentsUUIDs(new HashSet<>());
        }
        signedDocResults.forEach(signedDocResult ->
                currCert.getSignedDocumentsUUIDs().add(signedDocResult)
        );


        certificateService.save(currCert);


        return ResponseEntity.ok(signedDocResults);
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
        validateCertCategory(currCert);
        validateCertIssuers(currCert);


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
