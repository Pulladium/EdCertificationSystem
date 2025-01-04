package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.request.CertificateAddCategoryRequest;
import com.vozh.art.dataservice.dto.request.CreateCertificateRequest;
import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.request.ParticipantRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.dto.utils.CertificateMapper;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.Organization;
import com.vozh.art.dataservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data/certificates")
@RequiredArgsConstructor
@Slf4j
public class CertificateController {



    private final CertificateService certificateService;
    private final OrganizationService organizationService;

    // Logic for frontend
    @GetMapping("/pagingList")
    public ResponseEntity<Page<CertificateResponse>> getCertificates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CertificateResponse> certificatePage = certificateService.getCertificatesPagenated(pageRequest);

        return ResponseEntity.ok(certificatePage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificates(@PathVariable Long id) {
        log.trace("Getting certificates for user with id: {}", id);

        return ResponseEntity.ok(CertificateMapper.
                mapToResponse(certificateService.getById(id)));
    }

    /**
     * Create certificate
     *  String name;
     *  String description;
     *  can be setted later
     *  Set<CategoryRequest> categories = null; maybe not category request but their id. or use addCategory method
     *  can be setted later
     *  List<ParticipantRequest> certificateParticipants = null;
     * @param request
     * @return CertificateResponse
     */
    @PostMapping("/create")
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CreateCertificateRequest request) {
        log.trace("Creating certificate: {}", request);

        Certificate cert2Save = CertificateMapper.mapToCertificateEntity(request);

        Certificate savedCertificate = certificateService.save(cert2Save);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(savedCertificate));
    }


/**
     * Add category to certificate
     * @param request
     * @return CertificateResponse
     */
    @PostMapping("/addCategory")
    public ResponseEntity<CertificateResponse> addCategoryToCertificate(@RequestBody CertificateAddCategoryRequest request) {
        log.info("Adding category to certificate: {}", request);
        Certificate cert = certificateService.addCategoryToCertificate(request);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }


    @PutMapping("/addParticipant/{id}")
    public ResponseEntity<CertificateResponse> addParticipantToCertificate(@PathVariable Long id , @RequestBody ParticipantRequest request) {
        log.info("Adding participant to certificate: {}", request);
        Certificate cert = certificateService.addParticipantToCertificate(id, request);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }

    @PutMapping("/removeParticipant/{id}")
    public ResponseEntity<CertificateResponse> removeParticipantFromCertificate(@PathVariable Long id , @RequestBody ParticipantRequest request) {
        log.info("Removing participant from certificate: {}", request);
        Certificate cert = certificateService.removeParticipantFromCertificate(id, request);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }

    @PutMapping("/addIssuer/{id}")
    public ResponseEntity<CertificateResponse> addIssuerToCertificate(@PathVariable Long id , @RequestBody OrganizationRequest request) {
        log.info("Adding issuer to certificate: {}", request);
        OrganizationResponse orgRes = organizationService.createOrganization(request);

        Certificate cert = certificateService.addIssuersToCertificate(id, List.of(orgRes.getOrganizationId()));
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
    @PutMapping("/removeIssuer/{id}")
    public ResponseEntity<CertificateResponse> removeIssuerFromCertificate(@PathVariable Long id , @RequestBody OrganizationRequest request) {
        log.info("Removing issuer from certificate: {}", request);
        OrganizationResponse orgRes = organizationService.createOrganization(request);

        Certificate cert = certificateService.removeIssuersFromCertificate(id, List.of(orgRes.getOrganizationId()));
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
    @PutMapping("/addIssuersToCert/{id}")
    public ResponseEntity<CertificateResponse> addIssuersToCertificate(@PathVariable Long id , @RequestBody List<Long> orgIds) {
        log.info("Adding issuers to certificate: {}", orgIds);
        Certificate cert = certificateService.addIssuersToCertificate(id, orgIds);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
    @PutMapping("/removeIssuersFromCert/{id}")
    public ResponseEntity<CertificateResponse> removeIssuersFromCertificate(@PathVariable Long id , @RequestBody List<Long> orgIds) {
        log.info("Removing issuers from certificate: {}", orgIds);
        Certificate cert = certificateService.removeIssuersFromCertificate(id, orgIds);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }

}
