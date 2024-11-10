package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.repository.CertificateRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public Certificate getById(Long certificateID){
        Optional<Certificate> cert = certificateRepository.findById(certificateID);
        if(cert.isPresent()){
            return cert.get();
        }
        throw new PersistenceException("Cant find certificate by id");
    }

    public Certificate save(Certificate certificate){
        try {
            return certificateRepository.save(certificate);
        }
        catch (Exception e){
            throw new PersistenceException("Failed to save into DB");
        }
    }

    public static CertificateResponse mapToResponse(Certificate certificate){
        CertificateResponse response = CertificateResponse.builder()
                .certificateId(certificate.getId())
                .description(certificate.getDescription())
                .build();

        if(certificate.getCategories() != null){
            response.setCategories(certificate.getCategories().stream()
                    .map(category -> CategoryService.mapToResponse(category ,1))
                    .collect(Collectors.toSet()));
        }
        if (certificate.getIssuers() != null) {
            response.setIssuers(certificate.getIssuers().stream()
                    .map(issuer -> OrganizationService.mapToResponse(issuer))
                    .collect(Collectors.toSet()));
        }
        if (certificate.getCertificateParticipants() != null) {

            response.setParticipants(certificate.getCertificateParticipants().stream()
                    .map(participant -> ParticipantService.mapToResponse(participant.getParticipant()))
                    .collect(Collectors.toSet()));
        }
        return response;
    }


}
