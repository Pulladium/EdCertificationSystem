package com.vozh.art.dataservice.dto.request;

import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.service.CategoryService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

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

    private Set<CategoryRequest> subCategories;


}
