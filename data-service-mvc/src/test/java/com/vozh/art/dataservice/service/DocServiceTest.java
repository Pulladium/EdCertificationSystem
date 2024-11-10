package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.mongoDoc.SignedDoc;
import com.vozh.art.dataservice.repository.mDB.DocRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

//@ActiveProfiles("test")
@SpringBootTest(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
//@AutoConfigureMockMvc
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