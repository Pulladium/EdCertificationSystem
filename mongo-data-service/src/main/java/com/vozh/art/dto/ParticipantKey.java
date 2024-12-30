package com.vozh.art.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ParticipantKey {
    private String name;
    private String surname;
    private String email;
}
