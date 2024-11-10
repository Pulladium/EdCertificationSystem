package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.Organization;

public class OrganizationService {
    public static OrganizationResponse mapToResponse(Organization organization) {
        return OrganizationResponse.builder()
                .organizationId(organization.getId())
                .name(organization.getName())
                .address(organization.getAddress())
                .contactInfo(organization.getContactInfo())
                .status(organization.getStatus())
                .maintainerKeycloakUUID(organization.getMaintainerKeycloakUUID())
                .build();
    }
}