package com.vozh.art.dataservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateAddCategoryRequest {
    @NotNull
    private Long certificateId;
    @NotNull
    private Long categoryId;
}
