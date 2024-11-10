package com.vozh.art.dataservice.service;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@TestPropertySource(locations = "classpath:application.properties")
public class GridFsTests {
    @Autowired
    GridFSService gridFSService;


    @Test
    public void testSavingAndGettingFileNameById() throws IOException {
        MultipartFile file = new MockMultipartFile("MOCKingName", "mockingFile.doc", MediaType.APPLICATION_PDF_VALUE, "File DAta".getBytes());
        String gridfsFileUUID = gridFSService.storeFile(file);
        assertTrue(gridfsFileUUID.length() > 0);

        String fileName = gridFSService.getFileName(gridfsFileUUID);
        assertTrue(fileName.equals("mockingFile.doc"));

    }
    @Test
    public void testOnDeleteAndTryToRetriveThrowsFileNF() throws IOException {
        MultipartFile file = new MockMultipartFile("MOCKingName", "mockingFile.doc", MediaType.APPLICATION_PDF_VALUE, "File DAta".getBytes());
        String gridfsFileUUID = gridFSService.storeFile(file);
        assertTrue(gridfsFileUUID.length() > 0);

        gridFSService.deleteFile(gridfsFileUUID);

        assertThrows(IOException.class, () -> gridFSService.retrieveFile(gridfsFileUUID));
    }

}
