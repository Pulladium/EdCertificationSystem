package com.vozh.art.dataservice;

import com.vozh.art.dataservice.entity.*;
import com.vozh.art.dataservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class DataServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(DataServiceApplication.class);
    }

    @Bean
    public CommandLineRunner createCertificate(CertificateRepository certificateRepository,
                                               CategoryRepository categoryRepository,
                                               OrganizationRepository organizationRepository,
                                               CertificateParticipantRepo certificateParticipantRepo,
                                               ParticipantRepository participantRepository
    ){

        return args -> {
            Category category = Category.builder()
                    .description("Spring")
                    .build();

            Category subCategory = Category.builder()
                    .description("Spring boot")
                    .parentCategory(category)
                    .build();

            category.setSubCategories(Set.of(subCategory));
            subCategory.setParentCategory(category);

            Category subCategory2 = Category.builder()
                    .description("Spring boot")
                    .parentCategory(category)
                    .build();

            category.setSubCategories(Set.of(subCategory2));

            Organization organization = Organization.builder()
                    .name("NOT CTU")
                    .address("Terronska 2228")
                    .maintainerKeycloakUUID("Not seeted")
                    .status(Organization.OrganizationStatus.APPROVED)
                    .contactInfo("blablabla.der@gmail.com")
                    .build();



            Participant participant = Participant.builder()
                    .name("Jonh")
                    .surname("Svoboda")
                    .email("jouk.as@mail.cz")
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

            categoryRepository.saveAll(List.of(category));
            organizationRepository.save(organization);
            Certificate savedCert = certificateRepository.save(certificate);
            certificateParticipantRepo.save(certificateParticipant);
            log.info("Cert id after save : {}", savedCert.getId());
        };
    }

//    @Bean
//    public CommandLineRunner createData(DataItemRepository dataItemRepository) {
//        return args -> {
//            List<DataItem> items = Arrays.asList(
//                    DataItem.builder().name("Phone pro max").value("45").build(),
//                    DataItem.builder().name("Phone 12").value("12").build(),
//                    DataItem.builder().name("Phone 32").value("2").build()
//            );
//            dataItemRepository.saveAll(items);
//        };
//    }



}