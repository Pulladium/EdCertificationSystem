package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.CategoryRequest;
import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category saveUpdateCategory(Category category) {
        log.trace("Saving category: {} with {} subCategories", category,
                category.getSubCategories() == null? 0 : category.getSubCategories().size());
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Set<Category> getAllCategories() {
        return categoryRepository.findAllRootCategoriesWithSubCategories();
    }

    public CategoryResponse postNewCategory(CategoryRequest request){
        Category category = mapFromRequest(request);
        Category savedCategory = saveUpdateCategory(category);
        return mapToResponse(savedCategory, 2);
    }

    public CategoryResponse getCategoryResponseById(Long id, int depth) {
        Category category = getCategoryById(id);
        return mapToResponse(category, depth);
    }

    public Set<CategoryResponse> getAllCategoriesRespWithSubCatDepth(int depth){
        Set<Category> category = getAllCategories();

        return category.stream()
                .map(cat -> mapToResponse(cat, depth))
                .collect(Collectors.toSet());
    }




    public static CategoryResponse mapToResponse(Category category, int depth) {
        if (category == null || depth < 0) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder builder = CategoryResponse.builder()
                .categoryId(category.getId())
                .description(category.getDescription());

        builder.parentCategoryId(category.getParentCategory() == null ? null : category.getParentCategory().getId());

        if (depth > 0) {

            builder.subCategories(category.getSubCategories() != null ?
                    category.getSubCategories().stream()
                            .map(sub -> mapToResponse(sub, depth - 1))
                            .collect(Collectors.toSet()) : new HashSet<>());

        }

        return builder.build();
    }


}
