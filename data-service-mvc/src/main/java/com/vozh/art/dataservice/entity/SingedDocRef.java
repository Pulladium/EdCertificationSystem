package com.vozh.art.dataservice.entity;


import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SingedDocRef extends BaseEntity<Long> {
//need part id
    private String uuidOfDoc;

    private ParticipantKey participantKey;
}
