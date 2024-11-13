package com.vozh.art.dataservice.dto.utils;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
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

    public static Organization mapToEntity(OrganizationRequest organizationRequest) {
        return Organization.builder()
                .name(organizationRequest.getName())
                .address(organizationRequest.getAddress())
                .contactInfo(organizationRequest.getContactInfo())
//                status by admin
//                .status(organizationRequest.getStatus())
//                .maintainerKeycloakUUID(organizationRequest.getMaintainerKeycloakUUID())
//                TODO should be set by system already here
                .build();
    }
}
