package com.vozh.art.service;


import com.vozh.art.dto.SignedDocAndFile;
import com.vozh.art.entity.SignedDoc;
import com.vozh.art.repo.DocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

@Service
@RequiredArgsConstructor
public class DocService {
    private final DocRepository signedDocumentRepository;
    private final GridFsService gridFSService;
    private final KeyService keyStoreService;
    private final GridFsTemplate gridFsTemplate;

    public SignedDoc saveDocument(MultipartFile file) throws Exception {
        String gridFsFileId = gridFSService.storeFile(file);

        //с монго всё заебись работает
        // Генерируем новую пару ключей для каждого документа
        String keyAlias = "doc_" + System.currentTimeMillis();
        KeyPair keyPair = keyStoreService.generateKeyPair(keyAlias);

        // Генерируем подпись для файла
        String fileContent = new String(file.getBytes());
        String signature = keyStoreService.signData(fileContent, keyAlias);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
//TODO  not todo so i have base64 xd xd xd


        SignedDoc document = SignedDoc.builder()
                .name(file.getOriginalFilename())
                .fileType(file.getContentType())
                .gridFsFileId(gridFsFileId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .ed25519PublicKey(publicKeyBase64)
                .ed25519Signature(signature)
                .build();

        return signedDocumentRepository.save(document);
    }
    public Optional<SignedDoc> findById(String id) {
        return signedDocumentRepository.findById(id);
    }

    public InputStreamResource retrieveFile(String documentId) throws IOException {
        Optional<SignedDoc> document = signedDocumentRepository.findById(documentId);
        if (document.isPresent()) {
            return gridFSService.retrieveFile(document.get().getGridFsFileId());
        }
        throw new IOException("Document not found");
    }

    public void deleteDocument(String documentId) throws IOException {
        Optional<SignedDoc> document = signedDocumentRepository.findById(documentId);
        if (document.isPresent()) {
            gridFSService.deleteFile(document.get().getGridFsFileId());
            signedDocumentRepository.deleteById(documentId);
        } else {
            throw new IOException("Document not found");
        }
    }

    public List<SignedDoc> findAllDocuments() {
        return signedDocumentRepository.findAll();
    }

    public List<SignedDoc> findDocumentsByFileType(String fileType) {
        return signedDocumentRepository.findByFileType(fileType);
    }
    public boolean verifyDocumentById(String documentId, String publicKey, String signature) throws Exception {
        Optional<SignedDoc> documentOpt = signedDocumentRepository.findById(documentId);
        if (documentOpt.isPresent()) {
            SignedDoc document = documentOpt.get();
            InputStreamResource fileContent = gridFSService.retrieveFile(document.getGridFsFileId());
            String content = new String(fileContent.getInputStream().readAllBytes());

            return keyStoreService.verifySignature(content, signature, publicKey);
        }
        return false;
    }

    private SignedDoc getDocById(String id) {
        return signedDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
    }
    private byte[] getFileFromGridFs(String gridFsFileId) throws IOException {
        try {
            InputStreamResource fileContent = gridFSService.retrieveFile(gridFsFileId);
            byte[] content = IOUtils.toByteArray(fileContent.getInputStream());
            return  content;
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving file from GridFS", e);
        }
    }

    public SignedDocAndFile getSignedDocAndFile(String id) throws IOException {
        SignedDoc document = getDocById(id);
        byte[] content = getFileFromGridFs(document.getGridFsFileId());
        return new SignedDocAndFile(document, content);
    }



    public boolean verifyDocument(MultipartFile file, String publicKey, String signature) throws Exception {
        byte[] fileContent = file.getBytes();
        return keyStoreService.verifySignature(new String(fileContent), signature, publicKey);
    }
}
