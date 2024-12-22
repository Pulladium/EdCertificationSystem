package com.vozh.art.dataservice.dto.request;

import com.vozh.art.dataservice.entity.Organization;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String contactInfo;


//    uuid assigned by system. status assigned by admin
//    private Organization.OrganizationStatus status;
//    private String maintainerKeycloakUUID;

    public static OrganizationRequest fromOrganization(Organization organization){
        return OrganizationRequest.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .contactInfo(organization.getContactInfo())
                .build();
    }
    public static Organization toOrganization(OrganizationRequest organizationRequest){
        return Organization.builder()
                .name(organizationRequest.getName())
                .address(organizationRequest.getAddress())
                .contactInfo(organizationRequest.getContactInfo())
                .build();
    }
}
