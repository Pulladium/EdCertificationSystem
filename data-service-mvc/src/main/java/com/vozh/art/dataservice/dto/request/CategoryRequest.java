package com.vozh.art.dataservice.dto.request;

import com.vozh.art.dataservice.dto.response.CategoryResponse;
import jakarta.annotation.Nullable;
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

    private CategoryRequest parentCategory;
    private Set<CategoryRequest> subCategories;

}
