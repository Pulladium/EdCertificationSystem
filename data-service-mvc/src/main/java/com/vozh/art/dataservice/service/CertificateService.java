package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.CertificateAddCategoryRequest;
import com.vozh.art.dataservice.dto.request.ParticipantRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.*;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import com.vozh.art.dataservice.repository.CategoryRepository;
import com.vozh.art.dataservice.repository.CertificateRepository;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipantService participantService;
    private final CertificateParticipantService certificateParticipantService;
    private final OrganizationService organizationService;

    public Certificate getById(Long certificateID) {
        Optional<Certificate> cert = certificateRepository.findById(certificateID);
        if (cert.isPresent()) {
            return cert.get();
        }
        throw new PersistenceException("Cant find certificate by id");
    }

    public Set<Certificate> getAllByIds(List<Long> ids) {
        List<Certificate> certificates = certificateRepository.findAllById(ids);
        if (certificates.isEmpty()) {
            throw new PersistenceException("Cant find certificates by ids");
        }
        return new HashSet<>(certificates);
    }

    public Page<CertificateResponse> getCertificatesPagenated(PageRequest pageRequest) {
        Page<Certificate> certificatePage = findAll(pageRequest);
        return certificatePage.map(CertificateResponse::fromCertificate);
    }

    private Page<Certificate> findAll(Pageable pageRequest) {
        return certificateRepository.findAll(pageRequest);
    }

    private List<Certificate> findAll() {
        return certificateRepository.findAll();
    }


    public Certificate save(Certificate certificate) {
        try {
            return certificateRepository.save(certificate);
        } catch (Exception e) {
            throw new PersistenceException("Failed to save into DB");
        }
    }

    //todo maybe return CertificateResponse
    @Transactional
    public Certificate addCategoryToCertificate(CertificateAddCategoryRequest request) {
        Long certificateId = request.getCertificateId();
        Long categoryId = request.getCategoryId();


        Certificate certificate = getById(certificateId);
        Optional<Set<Category>> categories = Optional.ofNullable(certificate.getCategories());
        if (categories.isPresent()) {
            Optional<Category> category2Add = categoryRepository.findById(categoryId);
            if (category2Add.isEmpty()) {
                throw new PersistenceException("Category with id : " + categoryId + " not found");
            }


            //            duplicated code
            categories.get().add(category2Add.get());
            log.trace("Added category with id {} to certificate with id {}", categoryId, certificateId);
            certificate.setCategories(categories.get());
            log.trace("Set categories to certificate with id {}", certificateId);
            //            duplicated code
        } else {
            log.trace("Certificate with id {} has no categories, adding new Set of categories", certificateId);
            Optional<Category> category2Add = categoryRepository.findById(categoryId);
            if (category2Add.isEmpty()) {
                throw new PersistenceException("Category with id : " + categoryId + " not found");
            }

//            duplicated code
            Set<Category> newCategories = Set.of(category2Add.get());
            certificate.setCategories(newCategories);
            log.trace("Set categories to certificate with id {}", certificateId);
//            duplicated code
        }
//        todo do i save cat? i dont have cascade
        return save(certificate);
    }


    @Transactional
    public Certificate addParticipantToCertificate(Long id, ParticipantRequest request) {
        Certificate certificate = getById(id);
        if(certificate == null){
            throw new PersistenceException("Certificate with id " + id + " not found");
        }


        ParticipantKey participantKey = new ParticipantKey(request.getName(), request.getSurname(), request.getEmail());


        Participant participant = participantService.getParticipantByKey(participantKey);
        if(participant == null){
            participant = Participant.builder()
                    .participantKey(participantKey)
                    .build();
            participant = participantService.savePaticipant(participant);
        }

        final Long participantId = participant.getId();
        boolean participantExists = certificate.getCertificateParticipants().stream()
                .anyMatch(cp -> cp.getParticipant().getId().equals(participantId));

        if (participantExists) {
            throw new IllegalStateException("Participant already exists in this certificate");
        }


        CertificateParticipant certificateParticipant = CertificateParticipant.builder()
                .certificate(certificate)
                .participant(participant)
                .assignDate(LocalDateTime.now())
                .build();

        certificateParticipant = certificateParticipantService.save(certificateParticipant);

        certificate.getCertificateParticipants().add(certificateParticipant);
        return save(certificate);

    }


    @Transactional
    public Certificate removeParticipantFromCertificate(Long id, ParticipantRequest request) {
        Certificate certificate = getById(id);
        if(certificate == null){
            throw new PersistenceException("Certificate with id " + id + " not found");
        }

        ParticipantKey participantKey = new ParticipantKey(request.getName(), request.getSurname(), request.getEmail());
        Participant participant = participantService.getParticipantByKey(participantKey);
        if(participant == null){
            throw new PersistenceException("Participant with key " + participantKey + " not found");
        }

        //todo check if this works correctly should cascade save certificateParticipant>=?=
        CertificateParticipant certificateParticipant = certificate.getCertificateParticipants().stream()
                .filter(certPart -> certPart.getParticipant().equals(participant))
                .findFirst()
                .orElse(null);
        if(certificateParticipant == null){
            throw new PersistenceException("Participant with key " + participantKey + " not found in certificate with id " + id);
        }

        certificate.getCertificateParticipants().remove(certificateParticipant);
        //todo check if this works correctly should cascade save certificateParticipant>=?=
        return save(certificate);

    }


    @Transactional
    public Certificate addIssuersToCertificate(Long id, List<Long> issuerIds) {
        Certificate certificate = getById(id);
        if(certificate == null){
            throw new PersistenceException("Certificate with id " + id + " not found");
        }

        for (Long issuerId : issuerIds) {
            Organization issuer = organizationService.getOrganizationById(issuerId);
            if(issuer == null){
                throw new PersistenceException("Organization with id " + issuerId + " not found");
            }

            certificate.getIssuers().add(issuer);
        }

        return save(certificate);
    }
    @Transactional
    public Certificate removeIssuersFromCertificate(Long id, List<Long> issuerIds) {
        Certificate certificate = getById(id);
        if(certificate == null){
            throw new PersistenceException("Certificate with id " + id + " not found");
        }

        if(certificate.getIssuers() == null || certificate.getIssuers().isEmpty()) {
            throw new PersistenceException("Certificate has no issuers to remove");
        }

        for (Long issuerId : issuerIds) {
            Organization issuer = organizationService.getOrganizationById(issuerId);
            if(issuer == null){
                throw new PersistenceException("Organization with id " + issuerId + " not found");
            }

            certificate.getIssuers().remove(issuer);
        }

        return save(certificate);
    }

}
