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

}
