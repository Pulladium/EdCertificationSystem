package com.vozh.art.dataservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
//    todo need email validation
    @NotBlank
    private String email;

//    assigned by system
//    private LocalDateTime assignedAt;
}