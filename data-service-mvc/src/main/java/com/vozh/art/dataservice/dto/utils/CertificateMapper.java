package com.vozh.art.dataservice.dto.utils;

import com.vozh.art.dataservice.dto.request.CreateCertificateRequest;
import com.vozh.art.dataservice.dto.request.ParticipantRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.service.CategoryService;
import com.vozh.art.dataservice.service.CertificateParticipantService;
import com.vozh.art.dataservice.service.CertificateService;
import com.vozh.art.dataservice.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class CertificateMapper {

    private static CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService service) {
        CertificateMapper.categoryService = service;
    }

    private static ParticipantService participantService;
    @Autowired
    public void setParticipantService(ParticipantService service) {
        CertificateMapper.participantService = service;
    }

    private static CertificateParticipantService certificateParticipantService;
    @Autowired
    public void setCertificateParticipantService(CertificateParticipantService service) {
        CertificateMapper.certificateParticipantService = service;
    }



    public static CertificateResponse mapToResponse(Certificate certificate) {
        CertificateResponse response = CertificateResponse.builder()
                .certificateId(certificate.getId())
                .description(certificate.getDescription())
                .build();

        if (certificate.getCategories() != null) {
            response.setCategories(certificate.getCategories().stream()
                    .map(category -> CategoryMapper.mapToResponse(category, 1))
                    .collect(Collectors.toSet()));
        }
        if (certificate.getIssuers() != null) {
            response.setIssuers(certificate.getIssuers().stream()
                    .map(issuer -> OrganizationMapper.mapToResponse(issuer))
                    .collect(Collectors.toSet()));
        }
        if (certificate.getCertificateParticipants() != null) {

            response.setParticipants(certificate.getCertificateParticipants().stream()
                    .map(participant -> ParticipantMapper.mapToResponse(participant.getParticipant()))
                    .collect(Collectors.toSet()));
        }
        return response;
    }

    public static Certificate mapToCertificateEntity(CreateCertificateRequest request) {
        {
            Certificate certificate = Certificate.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .build();
            if (request.getCategoriesIds() != null) {
                certificate.setCategories(categoryService.getAllByIds(request.getCategoriesIds()));
            }
//                certificate.setCategories(request.getCategories().stream()
//                        .map(cat -> CategoryMapper.mapToCategoryEntity(cat))
//                        .collect(java.util.stream.Collectors.toSet()));
//            }
//            if (request.getCertificateParticipants() != null) {
//                List<Participant> addedPartic = participantService.addNewParticipants((List<ParticipantRequest>) request.getCertificateParticipants());
//
//                certificateParticipantService.assignParticipantsToCertificate(certificate.getId(), addedPartic.stream()
//                        .map(Participant::getId)
//                        .collect(Collectors.toList()));
//            }
            return certificate;
        }
    }

}
