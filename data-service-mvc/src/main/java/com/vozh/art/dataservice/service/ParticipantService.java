package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.ParticipantRequest;
import com.vozh.art.dataservice.dto.response.ParticipantResponse;
import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {
    public static ParticipantResponse mapToResponse(Participant participant) {
        return ParticipantResponse.builder()
                .participantId(participant.getId())
                .name(participant.getParticipantKey().getName())
                .surname(participant.getParticipantKey().getSurname())
                .email(participant.getParticipantKey().getEmail())
                .build();
    }

    public static Participant mapToEntity(ParticipantRequest participantRequest) {
        return Participant.builder()
                .participantKey(
                        new ParticipantKey(
                                participantRequest.getName(),
                                participantRequest.getSurname(),
                                participantRequest.getEmail()
                        )
                )
                .build();
    }
}
