package com.vozh.art.dataservice.entity.embedKey;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ParticipantKey implements Serializable {
    private String name;
    private String surname;
    private String email;
}
