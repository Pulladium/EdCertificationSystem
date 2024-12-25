package com.vozh.art.dataservice.dto.response;

import com.vozh.art.dataservice.entity.Certificate;
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

    private Set<SingedDocRefResponse> singedDocRefs;

    private Set<ParticipantResponse> participants;


    public static CertificateResponse fromCertificate(Certificate certificate){
        return CertificateResponse.builder()
                .certificateId(certificate.getId())
                .description(certificate.getDescription())
                .build();
    }
}
