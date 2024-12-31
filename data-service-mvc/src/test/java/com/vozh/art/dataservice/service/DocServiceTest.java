package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.mongoDoc.SignedDoc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@TestPropertySource(locations = "classpath:application.properties")
class DocServiceTest {

    @Autowired
    private DocService docService;


    @Test
    void verifyCertificateSingReturnTrue() throws Exception {

        MultipartFile file = new MockMultipartFile("mockingFile", "mockingFile.pdf", "application/pdf", "test data ".getBytes());
        SignedDoc singedDoc = docService.saveDocument(file);
        Assertions.assertTrue(docService.verifyDocument(file, singedDoc.getEd25519PublicKey(), singedDoc.getEd25519Signature()));

    }

    @Test
    void verifyCertificateUnSingedDoc() throws Exception {

        MultipartFile file = new MockMultipartFile("mockingFile", "mockingFile.pdf", "application/pdf", "test data ".getBytes());
        SignedDoc singedDoc = docService.saveDocument(file);
        MultipartFile file2 = new MockMultipartFile("mockingFile2", "mockingFile.pdf", "application/pdf", "unsignedtest data ".getBytes());
        Assertions.assertFalse(docService.verifyDocument(file2, singedDoc.getEd25519PublicKey(), singedDoc.getEd25519Signature()));

    }


}