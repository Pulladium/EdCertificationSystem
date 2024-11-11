package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.*;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import com.vozh.art.dataservice.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@TestPropertySource(locations = "classpath:application.properties")
public class CertificateTest {

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    CertificateParticipantRepo certificateParticipantRepo;

    @Autowired
    ParticipantRepository participantRepository;

    @Test
    public void testCreateCertificateWithRepos() {
        Category category = Category.builder()
                .description("Spring")
                .build();

        Category subCategory = Category.builder()
                .description("Spring boot")
                .parentCategory(category)
                .build();

        category.setSubCategories(Set.of(subCategory));
        subCategory.setParentCategory(category);


        Organization organization = Organization.builder()
                .name("NOT CTU")
                .address("Terronska 2228")
                .maintainerKeycloakUUID("Not seeted")
                .status(Organization.OrganizationStatus.APPROVED)
                .contactInfo("blablabla.der@gmail.com")
                .build();



        Participant participant = Participant.builder()
                .participantKey(new ParticipantKey(
                        "Bdsa",
                        "Waasss",
                        "Lolal.sd@main.com"
                ))
                .build();

        CertificateParticipant certificateParticipant = CertificateParticipant.builder()
                .participant(participant)
                .build();

        participant.setCertificateParticipants(Set.of(certificateParticipant));

        Certificate certificate = Certificate.builder()
                .description("Java Persistence qualification certificate")
                .categories(Set.of(subCategory))
                .issuers(Set.of(organization))
                .signedDocumentUUID(Set.of(new SingedDocRef("NOT YET")))
                .build();

        certificateParticipant.setCertificate(certificate);


        participantRepository.save(participant);

        categoryRepository.saveAll(List.of(category,subCategory));
        organizationRepository.save(organization);
        Certificate savedCert = certificateRepository.save(certificate);
        certificateParticipantRepo.save(certificateParticipant);

        assertTrue(savedCert.getId() != null);
        assertTrue(savedCert.getCategories().size() == 1);
        assertTrue(savedCert.getIssuers().size() == 1);

        assertTrue(savedCert.getIssuers().contains(organization));
        assertTrue(savedCert.getCategories().contains(subCategory));

    }

}
