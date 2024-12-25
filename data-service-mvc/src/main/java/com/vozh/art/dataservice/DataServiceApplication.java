package com.vozh.art.dataservice;

import com.vozh.art.dataservice.entity.*;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import com.vozh.art.dataservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableDiscoveryClient
@Slf4j
public class DataServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(DataServiceApplication.class);
    }

    @Bean
    @Transactional
    public CommandLineRunner createCertificate(CertificateRepository certificateRepository,
                                               CategoryRepository categoryRepository,
                                               OrganizationRepository organizationRepository,
                                               CertificateParticipantRepo certificateParticipantRepo,
                                               ParticipantRepository participantRepository
    ){

        return args -> {
            Category category = Category.builder()
                    .name("ABC")
                    .description("Spring")
                    .build();

            Category subCategory = Category.builder()
                    .name("BCD")
                    .description("Spring boot")
                    .parentCategory(category)
                    .build();

//            category.setSubCategories(Set.of(subCategory));
//            subCategory.setParentCategory(category);

            Category subCategory2 = Category.builder()
                    .name("CDE")
                    .description("Jpa Repos")
                    .parentCategory(category)
                    .build();

            Category sub2Category = Category.builder()
                    .name("DEF")
                    .description("Povesitsja")
                    .parentCategory(subCategory)
                    .build();
            Category category1 = Category.builder()
                    .name("GHI")
                    .description("googl cloud ")
                    .build();

            categoryRepository.save(category1);

            Set<Category> subCategories = new HashSet<>();
            subCategories.add(subCategory);
            subCategories.add(subCategory2);
            category.setSubCategories(subCategories);

            Organization organization = Organization.builder()
                    .name("NOT CTU")
                    .address("Terronska 2228")
                    .maintainerKeycloakUUID("Not seeted")
                    .status(Organization.OrganizationStatus.APPROVED)
                    .contactInfo("blablabla.der@gmail.com")
                    .build();



            Participant participant = Participant.builder()
                    .participantKey(new ParticipantKey("Bdsa", "Waasss", "Bdsad@gamdk.com"))
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

            Certificate certificate2 = Certificate.builder()
                    .description("Machine Learning qualification certificate")
                    .categories(Set.of(subCategory))
                    .issuers(Set.of(organization))
                    .signedDocumentUUID(Set.of(new SingedDocRef("NOT YET")))
                    .build();
            Certificate certificate3 = Certificate.builder()
                    .description("DevOps qualification certificate")
                    .categories(Set.of(subCategory))
                    .issuers(Set.of(organization))
                    .signedDocumentUUID(Set.of(new SingedDocRef("NOT YET")))
                    .build();
            Certificate certificate4 = Certificate.builder()
                    .description("Rust for beginners qualification certificate")
                    .categories(Set.of(subCategory))
                    .issuers(Set.of(organization))
                    .signedDocumentUUID(Set.of(new SingedDocRef("NOT YET")))
                    .build();

            Certificate certificate5 = Certificate.builder()
                    .description("IBM Cloud cli course certificate")
                    .categories(Set.of(subCategory))
                    .issuers(Set.of(organization))
                    .signedDocumentUUID(Set.of(new SingedDocRef("NOT YET")))
                    .build();
            Certificate certificate6 = Certificate.builder()
                    .description("AWS Cloud cli course certificate")
                    .categories(Set.of(subCategory))
                    .issuers(Set.of(organization))
                    .signedDocumentUUID(Set.of(new SingedDocRef("NOT YET")))
                    .build();
            certificateParticipant.setCertificate(certificate2);


            participantRepository.save(participant);

//            categoryRepository.saveAll(List.of(category,subCategory,subCategory2));
            categoryRepository.save(category);
            organizationRepository.save(organization);
//            List<Certificate> savedCert = certificateRepository.saveAll(List.of(certificate, certificate2));
            certificateRepository.saveAll(List.of(certificate, certificate2, certificate3, certificate4, certificate5, certificate6));
            certificateParticipantRepo.save(certificateParticipant);
//            log.info("Cert id after save : {}", savedCert.get(0).getId());
        };
    }




}