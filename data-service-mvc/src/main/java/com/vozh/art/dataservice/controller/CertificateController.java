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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PostMapping("/create")
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CreateCertificateRequest request) {
        log.trace("Creating certificate: {}", request);

        Certificate cert2Save = CertificateMapper.mapToCertificateEntity(request);
        cert2Save.setMaintainerKeycloakUUID(SecurityContextHolder.getContext().getAuthentication().getName());

        Certificate savedCertificate = certificateService.save(cert2Save);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(savedCertificate));
    }


/**
     * Add category to certificate
     * @param request
     * @return CertificateResponse
     */
    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PostMapping("/addCategory")
    public ResponseEntity<CertificateResponse> addCategoryToCertificate(@RequestBody CertificateAddCategoryRequest request) {
        validateCertAccessPermission(request.getCertificateId());
        log.info("Adding category to certificate: {}", request);
        Certificate cert = certificateService.addCategoryToCertificate(request);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }


    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PutMapping("/addParticipant/{id}")
    public ResponseEntity<CertificateResponse> addParticipantToCertificate(@PathVariable Long id , @RequestBody ParticipantRequest request) {
        validateCertAccessPermission(id);
        log.info("Adding participant to certificate: {}", request);
        Certificate cert = certificateService.addParticipantToCertificate(id, request);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
//    @PreAuthorize("hasRole('ROLE_registered-user')")
//    @PutMapping("/removeParticipant/{id}")
//    public ResponseEntity<CertificateResponse> removeParticipantFromCertificate(@PathVariable Long id , @RequestBody ParticipantRequest request) {
//        validateCertAccessPermission(id);
//        log.info("Removing participant from certificate: {}", request);
//        Certificate cert = certificateService.removeParticipantFromCertificate(id, request);
//        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
//    }
    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PutMapping("/removeParticipant/{id}")
    public ResponseEntity<CertificateResponse> removeParticipantFromCertificate(@PathVariable Long id , @RequestBody Long participantId) {
        validateCertAccessPermission(id);
        log.info("Removing participant from certificate: {}", participantId);
        Certificate cert = certificateService.removeParticipantFromCertificate(id, participantId);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }



    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PutMapping("/addIssuer/{id}")
    public ResponseEntity<CertificateResponse> addIssuerToCertificate(@PathVariable Long id , @RequestBody OrganizationRequest request) {
        validateCertAccessPermission(id);
        log.info("Adding issuer to certificate: {}", request);
        OrganizationResponse orgRes = organizationService.createOrganization(request);

        Certificate cert = certificateService.addIssuersToCertificate(id, List.of(orgRes.getOrganizationId()));
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PutMapping("/removeIssuer/{id}")
    public ResponseEntity<CertificateResponse> removeIssuerFromCertificate(@PathVariable Long id , @RequestBody OrganizationRequest request) {
        validateCertAccessPermission(id);
        log.info("Removing issuer from certificate: {}", request);
        OrganizationResponse orgRes = organizationService.createOrganization(request);

        Certificate cert = certificateService.removeIssuersFromCertificate(id, List.of(orgRes.getOrganizationId()));
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PutMapping("/addIssuersToCert/{id}")
    public ResponseEntity<CertificateResponse> addIssuersToCertificate(@PathVariable Long id , @RequestBody List<Long> orgIds) {
        validateCertAccessPermission(id);
        log.info("Adding issuers to certificate: {}", orgIds);
        Certificate cert = certificateService.addIssuersToCertificate(id, orgIds);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
    @PreAuthorize("hasRole('ROLE_registered-user')")
    @PutMapping("/removeIssuersFromCert/{id}")
    public ResponseEntity<CertificateResponse> removeIssuersFromCertificate(@PathVariable Long id , @RequestBody List<Long> orgIds) {
        validateCertAccessPermission(id);
        log.info("Removing issuers from certificate: {}", orgIds);
        Certificate cert = certificateService.removeIssuersFromCertificate(id, orgIds);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }


    private void validateCertAccessPermission(Long certId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current user authorities: {}",
                auth.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))
        );
        List<GrantedAuthority> userauthorities = auth.getAuthorities().stream().
                filter(a -> a.getAuthority().startsWith("ROLE_")).collect(Collectors.toList());

        if(userauthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_registered-user"))){
            if(auth.getName().equals(certificateService.getById(certId).getMaintainerKeycloakUUID())){
                return;
            }
        }
        if(userauthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"))){
            return;
        }
        throw new RuntimeException("You have no permission to update this organization");
    }
}
