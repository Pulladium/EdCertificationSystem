package com.vozh.art.dataservice.dto.request;

import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.dto.response.ParticipantResponse;
import com.vozh.art.dataservice.dto.response.SingedDocRefResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//dont use
public class CreateNewCertificateRequest {

    //todo add to entity
    @NotBlank
    private String name;
    @NotBlank
    private String description;

//    cant be empty every certificate must have at least one issuer,
//    even if it awaiting for approval or rejected
    @NotBlank
    private Set<OrganizationResponse> issuers;

    private Set<CategoryResponse> categories;

    private Set<SingedDocRefResponse> singedDocRefs;

    private Set<ParticipantResponse> participants;


}
