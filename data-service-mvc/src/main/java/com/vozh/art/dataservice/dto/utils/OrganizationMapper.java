package com.vozh.art.dataservice.dto.utils;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.request.UpdateOrgRequest;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.entity.BaseEntity;
import com.vozh.art.dataservice.entity.Organization;
import com.vozh.art.dataservice.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrganizationMapper {

    private static CertificateService certificateService;
    @Autowired
    public void setCertificateService(CertificateService service) {
        OrganizationMapper.certificateService = service;
    }


    public static OrganizationResponse mapToResponse(Organization organization) {
        OrganizationResponse organizationResponse = OrganizationResponse.builder()
                .organizationId(organization.getId())
                .name(organization.getName())
                .address(organization.getAddress())
                .contactInfo(organization.getContactInfo())
                .status(organization.getStatus())
                .maintainerKeycloakUUID(organization.getMaintainerKeycloakUUID())
                .build();

        //todo may throw null pointer
        organizationResponse.setCertificatesId(organization.getCertificates()
                .stream().map(BaseEntity::getId).collect(Collectors.toSet()));

        return organizationResponse;

    }

    //dosent map certificates
    public static Organization mapToEntity(OrganizationRequest organizationRequest) {
        Organization org =  Organization.builder()
                .name(organizationRequest.getName())
                .address(organizationRequest.getAddress())
                .contactInfo(organizationRequest.getContactInfo())
//                status by admin
//                .status(organizationRequest.getStatus())
//                .maintainerKeycloakUUID(organizationRequest.getMaintainerKeycloakUUID())
//                TODO should be set by system already here
                .build();

        if(organizationRequest instanceof UpdateOrgRequest updateOrgRequest){
            org.setId(updateOrgRequest.getOrganizationId());
            org.setCertificates(certificateService.getAllByIds(updateOrgRequest.getCertificatesIds()));
        }

        return org;
    }
}
