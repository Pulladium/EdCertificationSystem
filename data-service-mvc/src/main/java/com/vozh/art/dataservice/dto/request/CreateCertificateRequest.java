package com.vozh.art.dataservice.dto.request;


import com.vozh.art.dataservice.entity.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCertificateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String description;

    private Set<CategoryRequest> categories;

    //can set participants at start
    private Set<ParticipantRequest> certificateParticipants;

    //hz auto assign maybe if user org set
//    private Set<Organization> issuers;


    //cant be added by user
//    private Set<SingedDocRef> signedDocumentUUID;

    public static Certificate mapToCertificateEntity(CreateCertificateRequest request) {
        Certificate.CertificateBuilder builder = Certificate.builder()
                .name(request.getName())
                .description(request.getDescription());
        if (request.getCategories() != null) {
            builder.categories(request.getCategories().stream()
                    .map(CategoryRequest::mapToCategoryEntity)
                    .collect(java.util.stream.Collectors.toSet()));
        }
        if (request.getCertificateParticipants() != null) {
            builder.certificateParticipants(ParticipantRequest.mapToParticipantEntity(request.getCertificateParticipants()));
        }
    }
}
