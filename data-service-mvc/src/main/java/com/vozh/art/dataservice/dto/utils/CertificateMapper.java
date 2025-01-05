package com.vozh.art.dataservice.dto.utils;

import com.vozh.art.dataservice.dto.request.CreateCertificateRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public final class CertificateMapper {

    private static CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService service) {
        CertificateMapper.categoryService = service;
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
                    .map(certificateParticipant -> ParticipantMapper.mapToResponse(certificateParticipant))
                    .collect(Collectors.toSet()));
        }
        if(certificate.getSignedDocumentsUUIDs() != null){
            response.setSingedDocRefs(certificate.getSignedDocumentsUUIDs());
        }
        if(certificate.getMaintainerKeycloakUUID() != null){
            response.setMaintainerKeycloakUUID(certificate.getMaintainerKeycloakUUID());
        }else {
            log.error("CertificateMapper: Certificate maintainerKeycloakUUID is null");
            response.setMaintainerKeycloakUUID("null");
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
            return certificate;
        }
    }

}
