package com.vozh.art.dto.request;

import com.vozh.art.dto.ParticipantKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantTemplateRequest {
    private String templateName;
    private ParticipantKey participantKey;
}
