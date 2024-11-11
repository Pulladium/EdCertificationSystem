package com.vozh.art.dataservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Certificate entity maps GridFs files with Participants and Categories
 */

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate extends BaseEntity<Long> {

    private String name;
    private String description;
//    many issueres can issue many certificates
    @ManyToMany
    @JoinTable(
            name = "certificate_issuer",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private Set<Organization> issuers;

//    one certificate have all signed document for it
//    In SingDoc Object find by Participant UUID
    @OneToMany
    @JoinColumn(name = "document_id")
    private Set<SingedDocRef> signedDocumentUUID;


    @ManyToMany
    @JoinTable(
            name = "certificate_categories",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    //    one certificate can have many participants
    @OneToMany(mappedBy = "certificate")
    private Set<CertificateParticipant> certificateParticipants;


//    todo maybe add here method to get SingedDoc from signedDocumentUUID

//    @OneToMany(mappedBy = "certificate", cascade = CascadeType.ALL)
//    private Set<CertificateParticipant> certificateParticipants = new HashSet<>();
}
