package com.vozh.art.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
@Document(collection = "signed_documents")
public class SignedDoc {
    //todo teset it
    //MongoDB has its own ID generation strategy, so @GeneratedValue is not needed
//    https://stackoverflow.com/questions/69732588/spring-boot-mongodb-can-generatedvalue-and-column-annotations-be-used
    @Id
    private String id;

    private String name;

    private String fileType;

    private String gridFsFileId; // ID файла в GridFS

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String ed25519PublicKey;

    private String ed25519Signature;

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", ed25519PublicKey='" + ed25519PublicKey + '\'' +
                ", ed25519Signature='" + ed25519Signature + '\'' +
                '}';
    }
}
