package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.request.CertificateAddCategoryRequest;
import com.vozh.art.dataservice.dto.request.CreateCertificateRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.service.CategoryService;
import com.vozh.art.dataservice.service.CertificateParticipantService;
import com.vozh.art.dataservice.service.CertificateService;
import com.vozh.art.dataservice.service.ParticipantService;
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
    private final CategoryService categoryService;
    private final CertificateParticipantService certificateParticipantService;
    private final ParticipantService participantService;


    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificates(@PathVariable Long id) {
        log.trace("Getting certificates for user with id: {}", id);

        return ResponseEntity.ok(CertificateService.
                mapToResponse(certificateService.getById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CreateCertificateRequest request) {
        log.trace("Creating certificate: {}", request);

        Certificate savedCertificate = certificateService.save(CertificateService.mapToCertificateEntity(request, categoryService,participantService,certificateParticipantService));
        return ResponseEntity.ok(CertificateService.mapToResponse(savedCertificate));
    }

    @PostMapping("/addCategory")
    public ResponseEntity<CertificateResponse> addCategoryToCertificate(@RequestBody CertificateAddCategoryRequest request) {
        log.info("Adding category to certificate: {}", request);
        Certificate cert = certificateService.addCategoryToCertificate(request);
        return ResponseEntity.ok(CertificateService.mapToResponse(cert));
    }
}
