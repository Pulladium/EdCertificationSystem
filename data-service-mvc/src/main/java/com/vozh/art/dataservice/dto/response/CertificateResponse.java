package com.vozh.art.dataservice.dto.response;

import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.SingedDocRef;
import com.vozh.art.dataservice.repository.SignedDocRefRepo;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateResponse {
    private Long certificateId;
    private String description;

    private Set<OrganizationResponse> issuers;

    private Set<CategoryResponse> categories;

//    private Set<SingedDocRefResponse> singedDocRefs;

    private Set<SingedDocRef> singedDocRefs;

    private Set<ParticipantResponse> participants;


    private String maintainerKeycloakUUID;

    //todo uses this method for paging logic
    public static CertificateResponse fromCertificate(Certificate certificate){
        return CertificateResponse.builder()
                .certificateId(certificate.getId())
                .description(certificate.getDescription())
                .build();
    }
}
