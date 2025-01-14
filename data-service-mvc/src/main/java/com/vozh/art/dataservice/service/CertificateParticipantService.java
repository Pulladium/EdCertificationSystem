package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.CertificateParticipant;
import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.repository.CertificateParticipantRepo;
import com.vozh.art.dataservice.repository.CertificateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateParticipantService {

    private final CertificateRepository certificateRepository;
    private final ParticipantService participantService;
    private final CertificateParticipantRepo certificateParticipantRepo;
    @Transactional
    public void assignParticipantsToCertificate(Long certificateId, List<Long> participantsIds) {
        for (Long id : participantsIds) {
            Participant currPart = participantService.getById(id);
            CertificateParticipant certificateParticipant = new CertificateParticipant();
            certificateParticipant.setCertificate(certificateRepository.getById(certificateId));
            certificateParticipant.setParticipant(currPart);
            certificateParticipant.setAssignDate(LocalDateTime.now());
            certificateParticipantRepo.save(certificateParticipant);
        }
    }

    public CertificateParticipant save(CertificateParticipant certificateParticipant) {
        return certificateParticipantRepo.save(certificateParticipant);
    }

    public CertificateParticipant findById(Long id) {
        CertificateParticipant certificateParticipant = certificateParticipantRepo.findById(id).orElse(null);
        if (certificateParticipant == null) {
            throw new EntityNotFoundException("CertificateParticipant with id " + id + " not found");
        }
        return certificateParticipant;
    }
}
