package com.vozh.art.dataservice.entity;

import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import jakarta.persistence.Entity;
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
public class Participant extends BaseEntity<Long>{
    @OneToMany(mappedBy = "participant")
    private Set<CertificateParticipant> certificateParticipants;

    private ParticipantKey participantKey;
}
