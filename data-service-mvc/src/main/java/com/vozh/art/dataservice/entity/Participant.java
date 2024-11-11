package com.vozh.art.dataservice.entity;

import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries(
        {
                @NamedQuery(name = "Participant.findByParticipantKey",
                        query = "SELECT p FROM Participant p WHERE p.participantKey.name = :name " +
                                "AND p.participantKey.surname = :surname" +
                                " AND p.participantKey.email = :email")
        }
)
public class Participant extends BaseEntity<Long>{
    @OneToMany(mappedBy = "participant")
    private Set<CertificateParticipant> certificateParticipants;

    private ParticipantKey participantKey;
}
