package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.CertificateParticipant;
import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.repository.CertificateParticipantRepo;
import com.vozh.art.dataservice.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateParticipantService {

    CertificateRepository certificateRepository;
    ParticipantService participantService;
    CertificateParticipantRepo certificateParticipantRepo;
    public void assignParticipantToCertificate(Long certificateId, List<Long> participantId) {
        for (Long id : participantId) {
            Participant currPart = participantService.getById(id);
            CertificateParticipant certificateParticipant = new CertificateParticipant();
            certificateParticipant.setCertificate(certificateRepository.getById(certificateId));
            certificateParticipant.setParticipant(currPart);
            certificateParticipant.setAssignDate(LocalDateTime.now());
            certificateParticipantRepo.save(certificateParticipant);
        }
    }
}
