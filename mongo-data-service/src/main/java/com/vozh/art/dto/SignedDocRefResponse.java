package com.vozh.art.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class SignedDocRefResponse {
    private String uuidOfDoc;
    private ParticipantKey participantKey;
}
