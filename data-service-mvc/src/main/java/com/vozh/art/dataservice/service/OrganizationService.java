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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

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
        Organization savedOrganization = organizationRepository.save(organization);
        log.trace("Mapping saved organization to response: {}", savedOrganization);
        return OrganizationResponse.fromOrganization(savedOrganization);
    }

    public OrganizationResponse getOrganizationResponseById(Long id) {
        log.trace("Getting organization by id: {}", id);
        Organization organization = getOrganizationById(id);
        return OrganizationResponse.fromOrganization(organization);
    }

    @Transactional
    public boolean deleteOrganizationById(Long id) {
        log.trace("Deleting organization by id: {}", id);
        organizationRepository.deleteById(id);
        return true;
    }

    @Transactional
    public OrganizationResponse updateOrganization(UpdateOrgRequest request){
        log.trace("Updating organization: {}", request);
        validateOrgUpdate(request);
        Organization organization = organizationRepository.findById(request.getOrganizationId()).orElseThrow();
        //todo make all list or set or check custing
        organization.setCertificates((Set<Certificate>) certificateRepository.findAllById(request.getCertificateIds()));
        Organization savedOrganization = organizationRepository.save(organization);
        return OrganizationResponse.fromOrganization(savedOrganization);
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
        request.getCertificateIds().forEach(certificateId -> {
            //throws NoSuchElementException if not found
            Certificate certificate = certificateRepository.findById(certificateId).orElseThrow();
            validateCertHaveOneMoreOrg(certificate,organization);
        });
        return true;
    }

    @Transactional
    public OrganizationResponse approveOrganization(Long id){
        log.trace("Approving organization by id: {}", id);
        Organization organization = getOrganizationById(id);
        organization.setStatus(Organization.OrganizationStatus.APPROVED);
        Organization savedOrganization = organizationRepository.save(organization);
        return OrganizationResponse.fromOrganization(savedOrganization);
    }
    @Transactional
    public OrganizationResponse rejectOrganization(Long id){
        log.trace("Rejecting organization by id: {}", id);
        Organization organization = getOrganizationById(id);
        organization.setStatus(Organization.OrganizationStatus.REJECTED);
        Organization savedOrganization = organizationRepository.save(organization);
        return OrganizationResponse.fromOrganization(savedOrganization);
    }

    @GetMapping("/user-info")
    public String getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject(); // This is the user's UUID
        String email = jwt.getClaim("email");
        // Access other claims as needed
        return "User ID: " + userId + ", Email: " + email;
    }


    private Organization getOrganizationById(Long id) {
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
        return organizationPage.map(OrganizationResponse::fromOrganization);
    }
    private Page<Organization> findAll(Pageable pageRequest) {
        return organizationRepository.findAll(pageRequest);
    }
}
