package com.vozh.art.dataservice.dto.response;


import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String description;

    private CategoryResponse parentCategory;
    private Set<CategoryResponse> subCategories;
}
