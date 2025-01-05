package com.vozh.art.controller;

import com.vozh.art.dto.SignedDocAndFile;
import com.vozh.art.entity.SignedDoc;
import com.vozh.art.service.DocService;
import com.vozh.art.service.KeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@RestController
@RequestMapping("/api/data-mongo/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocService signedDocService;
    private final KeyService keyStoreService;

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) throws Exception {
        SignedDoc document = signedDocService.saveDocument(file);

        Map<String, String> response = Map.of(
                "publicKey", document.getEd25519PublicKey(),
                "signature", document.getEd25519Signature() + " doc uuid = " +  document.getId()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<Boolean> verifyDocument(@PathVariable String id,
                                                  @RequestBody Map<String, String> verificationData) throws Exception {
        String publicKey = verificationData.get("publicKey");
        String signature = verificationData.get("signature");

        boolean isValid = signedDocService.verifyDocumentById(id, publicKey, signature);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("publicKey") String publicKey,
            @RequestParam("signature") String signature) throws Exception {

        boolean isValid = signedDocService.verifyDocument(file, publicKey, signature);
        return ResponseEntity.ok(isValid);
    }


    //todo should decode file name if it was in Russian on Client side,
    // http headers supports only utf8
//    @GetMapping("/{id}")
//    public ResponseEntity<ByteArrayResource> getDocument(@PathVariable String id) throws IOException {
    ////        DocWithFile documentWithContent = signedDocService.getDocumentWithContent(id);
//        SignedDoc document = documentWithContent.getDocument();
//        byte[] content = documentWithContent.getContent();
//
//        ByteArrayResource resource = new ByteArrayResource(content);
//
//        String fileName = document.getName();
//        String fileExtension = FilenameUtils.getExtension(fileName);
//        String fileNameWithoutExtension = FilenameUtils.getBaseName(fileName);
//
//        String encodedFileName = URLEncoder.encode(fileNameWithoutExtension, StandardCharsets.UTF_8.toString())
//                .replaceAll("\\+", "%20");
//
//        String contentDisposition = String.format("attachment; filename=\"%s.%s\"; filename*=UTF-8''%s.%s",
//                encodedFileName, fileExtension, encodedFileName, fileExtension);
//
//        log.warn(document.getName());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .contentType(MediaType.parseMediaType(document.getFileType()))
//                .contentLength(content.length)
//                .body(resource);
//    }

    //todo should also send verification data
    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getDocumentFile (@PathVariable String id) throws IOException {
        SignedDocAndFile documentWithContent = signedDocService.getSignedDocAndFile(id);
        SignedDoc document = documentWithContent.doc();
        byte[] content = documentWithContent.file();

        ByteArrayResource resource = new ByteArrayResource(content);

        String fileName = document.getName();
        String fileExtension = FilenameUtils.getExtension(fileName);
        String fileNameWithoutExtension = FilenameUtils.getBaseName(fileName);

        String encodedFileName = URLEncoder.encode(fileNameWithoutExtension, StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");

        String contentDisposition = String.format("attachment; filename=\"%s.%s\"; filename*=UTF-8''%s.%s",
                encodedFileName, fileExtension, encodedFileName, fileExtension);

        log.warn(document.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .contentLength(content.length)
                .body(resource);
    }
}