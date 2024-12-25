package com.vozh.art.dataservice.dto.response;

import com.vozh.art.dataservice.entity.Organization;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationResponse {
    private Long organizationId;

//    todo from where called this field
//    private Set<CertificateResponse> certificates;

    private String name;
    private String address;
    private String contactInfo;
    private Organization.OrganizationStatus status;
    private String maintainerKeycloakUUID;

    public static OrganizationResponse fromOrganization(Organization organization){
        return OrganizationResponse.builder()
                .organizationId(organization.getId())
                .name(organization.getName())
                .address(organization.getAddress())
                .contactInfo(organization.getContactInfo())
                .status(organization.getStatus())
                .maintainerKeycloakUUID(organization.getMaintainerKeycloakUUID())
                .build();
    }
    public static Organization toOrganization(OrganizationResponse organizationResponse){
        return Organization.builder()
                .id(organizationResponse.getOrganizationId())
                .name(organizationResponse.getName())
                .address(organizationResponse.getAddress())
                .contactInfo(organizationResponse.getContactInfo())
                .status(organizationResponse.getStatus())
                .maintainerKeycloakUUID(organizationResponse.getMaintainerKeycloakUUID())
                .build();
    }
}
