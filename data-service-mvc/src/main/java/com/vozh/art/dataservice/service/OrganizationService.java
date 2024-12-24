package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.Organization;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.Organization;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationResponse createOrganization(OrganizationRequest request){
        log.trace("Mapping request to organization: {}", request);
        Organization organization = OrganizationRequest.toOrganization(request);
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

    public boolean deleteOrganizationById(Long id) {
        log.trace("Deleting organization by id: {}", id);
        organizationRepository.deleteById(id);
        return true;
    }

    @Transactional
    public OrganizationResponse updateOrganization(OrganizationRequest request){
        log.trace("Mapping request to organization: {}", request);
        Organization organization = OrganizationRequest.toOrganization(request);
        log.trace("Saving organization via organization_repository: {}", organization);
        Organization savedOrganization = organizationRepository.save(organization);
        log.trace("Mapping saved organization to response: {}", savedOrganization);
        return OrganizationResponse.fromOrganization(savedOrganization);
    }

    @Transactional
    public OrganizationResponse approveOrganization(Long id){
        log.trace("Approving organization by id: {}", id);
        Organization organization = getOrganizationById(id);
        organization.setStatus(Organization.OrganizationStatus.APPROVED);
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
