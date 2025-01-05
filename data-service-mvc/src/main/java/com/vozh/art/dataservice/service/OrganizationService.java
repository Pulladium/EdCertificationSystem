package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.UpdateOrgRequest;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.dto.utils.OrganizationMapper;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.Organization;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.repository.CertificateRepository;
import com.vozh.art.dataservice.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final CertificateRepository certificateRepository;

    @Transactional
    public OrganizationResponse createOrganization(OrganizationRequest request){
        log.trace("Mapping request to organization: {}", request);
        Organization organization = OrganizationMapper.mapToEntity(request);
        log.trace("Saving organization via organization_repository: {}", organization);
        organization.setStatus(Organization.OrganizationStatus.AWAITING);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        organization.setMaintainerKeycloakUUID(auth.getName());
        Organization savedOrganization = organizationRepository.save(organization);
        log.trace("Mapping saved organization to response: {}", savedOrganization);
//        return OrganizationResponse.fromOrganization(savedOrganization);
        return OrganizationMapper.mapToResponse(savedOrganization);
    }

    public OrganizationResponse getOrganizationResponseById(Long id) {
        log.trace("Getting organization by id: {}", id);
        Organization organization = getOrganizationById(id);
//        return OrganizationResponse.fromOrganization(organization);
        return OrganizationMapper.mapToResponse(organization);
    }

    @Transactional
    public boolean deleteOrganizationById(Long id) {
        log.trace("Deleting organization by id: {}", id);
        Organization organization2Delete = getOrganizationById(id);
        List<Certificate> certificates = organization2Delete.getCertificates().stream().collect(Collectors.toList());
        certificates.forEach(cert -> {
            validateCertHaveOneMoreOrg(cert, organization2Delete);
            cert.getIssuers().remove(organization2Delete);
            certificateRepository.save(cert);
        });
        organizationRepository.deleteById(id);
        return true;
    }

//    @Transactional
//    public OrganizationResponse updateOrganization(UpdateOrgRequest request){
//        log.trace("Updating organization: {}", request);
//        validateOrgUpdate(request);
//        Organization organizationFromRepo = organizationRepository.findById(request.getOrganizationId()).orElseThrow();
//        Organization organization = OrganizationMapper.mapToEntity(request);
//
//        organization.setStatus(organizationFromRepo.getStatus());
//        organization.setMaintainerKeycloakUUID(organizationFromRepo.getMaintainerKeycloakUUID());
//
//        //todo make all list or set or check custing
//        List<Certificate> certificateList = certificateRepository.findAllById(request.getCertificatesIds());
//        Set<Certificate> certificateSet = new HashSet<>(certificateList);
//
//        organization.setCertificates(certificateSet);
//
//        Organization savedOrganization = organizationRepository.save(organization);
//        return OrganizationMapper.mapToResponse(savedOrganization);
//    }
    @Transactional
    public OrganizationResponse updateOrganization(UpdateOrgRequest request) {
        log.trace("Updating organization: {}", request);
        validateOrgUpdate(request);

        Organization organization = organizationRepository.findById(request.getOrganizationId()).orElseThrow();

        // Update fields
        organization.setName(request.getName());
        organization.setAddress(request.getAddress());
        organization.setContactInfo(request.getContactInfo());

        // Get new certificates
        List<Certificate> newCertificates = certificateRepository.findAllById(request.getCertificatesIds());

        // Remove organization from old certificates that are not in new list
        organization.getCertificates().forEach(cert -> {
            if (!newCertificates.contains(cert)) {
                cert.getIssuers().remove(organization);
                certificateRepository.save(cert);
            }
        });

        // Add organization to new certificates
        newCertificates.forEach(cert -> {
            if (!cert.getIssuers().contains(organization)) {
                cert.getIssuers().add(organization);
                certificateRepository.save(cert);
            }
        });

        // Update organization's certificates set
        organization.setCertificates(new HashSet<>(newCertificates));

        return OrganizationMapper.mapToResponse(organizationRepository.save(organization));
    }

// get all organizations
    private void validateCertHaveOneMoreOrg(Certificate certificate, Organization org2Remove){
        if(certificate.getIssuers().size() == 1 && certificate.getIssuers().contains(org2Remove)){
            log.error("Certificate with id: {} has only one organization, cant remove it", certificate.getId());
            throw new RuntimeException("Certificate with id: " + certificate.getId() + " has only one organization, cant remove it");
        }
    }

    private boolean validateOrgUpdate(UpdateOrgRequest request){
        final Organization organization = organizationRepository.findById(request.getOrganizationId()).orElseThrow();
        Set<Long> currentCertificatesIds = organization.getCertificates().stream().map(Certificate::getId).collect(Collectors.toSet());

        Set<Long> newCertificatesIds = request.getCertificatesIds().stream().collect(Collectors.toSet());

        //  removed certificates
        Set<Long> removedCertificatesIds = new HashSet<>(currentCertificatesIds);
        removedCertificatesIds.removeAll(newCertificatesIds);

        //  added certificates
        Set<Long> addedCertificatesIds = new HashSet<>(newCertificatesIds);
        addedCertificatesIds.removeAll(currentCertificatesIds);

        for (Long certId : removedCertificatesIds) {
            Certificate certificate = certificateRepository.findById(certId).orElseThrow();
            validateCertHaveOneMoreOrg(certificate, organization);
        }
        for(Long certId : addedCertificatesIds) {
            Certificate certificate = certificateRepository.findById(certId).orElseThrow();
            if (certificate.getIssuers().contains(organization)) {
                log.error("Certificate with id: {} already has organization with id: {}", certId, organization.getId());
                throw new RuntimeException("Certificate with id: " + certId + " already has organization with id: " + organization.getId());
            }
        }

        return true;
    }

    @Transactional
    public OrganizationResponse approveOrganization(Long id){
        log.trace("Approving organization by id: {}", id);
        Organization organization = getOrganizationById(id);
        organization.setStatus(Organization.OrganizationStatus.APPROVED);
        Organization savedOrganization = organizationRepository.save(organization);
//        return OrganizationResponse.fromOrganization(savedOrganization);
        return OrganizationMapper.mapToResponse(savedOrganization);
    }
    @Transactional
    public OrganizationResponse rejectOrganization(Long id){
        log.trace("Rejecting organization by id: {}", id);
        Organization organization = getOrganizationById(id);
        organization.setStatus(Organization.OrganizationStatus.REJECTED);
        Organization savedOrganization = organizationRepository.save(organization);
//        return OrganizationResponse.fromOrganization(savedOrganization);
        return OrganizationMapper.mapToResponse(savedOrganization);
    }

    @GetMapping("/user-info")
    public String getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject(); // This is the user's UUID
        String email = jwt.getClaim("email");
        // Access other claims as needed
        return "User ID: " + userId + ", Email: " + email;
    }


    public Organization getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElse(null);
        if(organization == null){
            log.error("Organization with id: {} not found", id);
            //todo controler advice and exception handling
            throw new RuntimeException("Organization with id: " + id + " not found");
        }
        return organization;
    }



    public Page<OrganizationResponse> getOrganizationsPagenated(PageRequest pageRequest) {
//        Page<>
        Page<Organization> organizationPage = findAll(pageRequest);
//        return organizationPage.map(OrganizationResponse::fromOrganization);
        return organizationPage.map(OrganizationMapper::mapToResponse);
    }
    private Page<Organization> findAll(Pageable pageRequest) {
        return organizationRepository.findAll(pageRequest);
    }
}
