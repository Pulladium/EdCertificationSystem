package com.vozh.art.dataservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    private Long parentCategoryId;


    //todo ids or remove
    private Set<Long> subCategoriesIds;


}
