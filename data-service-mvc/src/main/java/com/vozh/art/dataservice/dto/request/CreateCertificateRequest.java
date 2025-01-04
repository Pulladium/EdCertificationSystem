package com.vozh.art.dataservice.dto.request;


import com.vozh.art.dataservice.entity.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCertificateRequest {

    //or title on js
    @NotBlank
    private String name;
    @NotBlank
    private String description;


    // maybe id????????
    private List<Long> categoriesIds;

    //can set participants at start spis not
//    private List<ParticipantRequest> certificateParticipants;

    //hz auto assign maybe if user org set
//    private Set<Organization> issuers;


    //cant be added by user
//    private Set<SingedDocRef> signedDocumentUUID;


}
