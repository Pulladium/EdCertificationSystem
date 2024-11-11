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
//    todo need name
    @NotBlank
    private String description;
//
//    @Nullable
    private CategoryResponse parentCategory;
    private Set<CategoryResponse> subCategories;

}
