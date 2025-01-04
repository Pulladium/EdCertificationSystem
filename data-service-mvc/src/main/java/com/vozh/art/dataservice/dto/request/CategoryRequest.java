package com.vozh.art.dataservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class CategoryRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String description;



    private Set<Long> subCategoriesIds;


}
