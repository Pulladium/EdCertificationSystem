package com.vozh.art.repo;

import com.vozh.art.entity.SignedDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DocRepository extends MongoRepository<SignedDoc, String> {

    Optional<SignedDoc> findByName(String name);

    List<SignedDoc> findByFileType(String fileType);


    List<SignedDoc> findByCreatedAtAfter(LocalDateTime date);

    List<SignedDoc> findByEd25519PublicKey(String publicKey);

    List<SignedDoc> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByGridFsFileId(String gridFsFileId);

    // Удалить документ по GridFS ID
    void deleteByGridFsFileId(String gridFsFileId);

    // Найти все документы, отсортированные по дате создания (в порядке убывания)
    List<SignedDoc> findAllByOrderByCreatedAtDesc();

    // Поиск документов, содержащих определенную подстроку в имени (без учета регистра)
    List<SignedDoc> findByNameContainingIgnoreCase(String nameSubstring);
}