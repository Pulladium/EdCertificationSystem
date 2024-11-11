package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.CategoryRequest;
import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
//    public Category createCategory(Category category) {
//        return categoryRepository.save(category);
//    }

    public Category saveUpdateCategory(Category category) {
        log.trace("Saving category: {} with {} subCategories", category, category.getSubCategories().size());
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }





    public static Category mapFromRequest(CategoryRequest request){
        if(request == null){
            throw new IllegalArgumentException("Request is null");
        }
        Category catFromRequest = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        if(request.getParentCategory() != null){
            catFromRequest.setParentCategory(mapFromRequest(request.getParentCategory()));
        }
        if(request.getSubCategories() != null){
            catFromRequest.setSubCategories(request.getSubCategories().stream()
                    .map(CategoryService::mapFromRequest)
                    .collect(Collectors.toSet()));
        }
        return catFromRequest;

    }
    public static CategoryResponse mapToResponse(Category category, int depth) {
        if (category == null || depth < 0) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder builder = CategoryResponse.builder()
                .categoryId(category.getId())
                .description(category.getDescription());

        if (depth > 0) {
            builder.parentCategory(mapToResponse(category.getParentCategory(), depth - 1))
                    .subCategories(category.getSubCategories() != null ?
                            category.getSubCategories().stream()
                                    .map(sub -> mapToResponse(sub, depth - 1))
                                    .collect(Collectors.toSet()) : new HashSet<>());
        }

        return builder.build();
    }

}
