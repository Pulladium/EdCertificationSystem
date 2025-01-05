package com.vozh.art.dataservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_certificate_name_maintainer",
                columnNames = {"name", "maintainerKeycloakUUID"}
        )
})
public class Certificate extends BaseEntity<Long> {

    private String name;
    private String description;


    @ManyToMany
    @JoinTable(
            name = "certificate_issuer",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private Set<Organization> issuers;


    @OneToMany
    @JoinColumn(name = "document_id")
    private Set<SingedDocRef> signedDocumentsUUIDs;


    @ManyToMany
    @JoinTable(
            name = "certificate_categories",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    //    one certificate can have many participants
    @OneToMany(mappedBy = "certificate",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<CertificateParticipant> certificateParticipants;



    private String maintainerKeycloakUUID;

}
