package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.request.CertificateAddCategoryRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@Slf4j
public class CertificateController {



    private final CertificateService certificateService;
    @GetMapping("{id}")
    public ResponseEntity<CertificateResponse> getCertificates(@PathVariable Long id) {
        log.info("Getting certificates for user with id: {}", id);

        return ResponseEntity.ok(CertificateService.
                mapToResponse(certificateService.getById(id)));
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/create")
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate certificate) {
        log.info("Creating certificate: {}", certificate);
        return ResponseEntity.ok(certificateService.save(certificate));
    }

    @PostMapping("/addCategory")
    public ResponseEntity<CertificateResponse> addCategoryToCertificate(@RequestBody CertificateAddCategoryRequest request) {
        log.info("Adding category to certificate: {}", request);
        Certificate cert = certificateService.addCategoryToCertificate(request);
        return ResponseEntity.ok(CertificateService.mapToResponse(cert));
    }
}
