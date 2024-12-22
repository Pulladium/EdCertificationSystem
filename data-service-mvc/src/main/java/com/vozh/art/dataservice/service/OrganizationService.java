package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.Organization;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.Organization;
import com.vozh.art.dataservice.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    private Organization getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElse(null);
        if(organization == null){
            log.error("Organization with id: {} not found", id);
            //todo controler advice and exception handling
            throw new RuntimeException("Organization with id: " + id + " not found");
        }
        return organization;
    }


}
