package com.vozh.art.dataservice.validate;

import com.vozh.art.dataservice.dto.request.CategoryRequest;
import com.vozh.art.dataservice.dto.request.CreateCatRequest;
import com.vozh.art.dataservice.dto.request.UpdateCatRequest;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.service.CategoryService;

import java.util.HashSet;
import java.util.Set;

public class CategoryRequestValidator {

    /**
     * Проверяет что parentId не находится в списке subCategoriesIds
     */
    public static void validateParentChildCircularReference(CategoryRequest request) {
        if (request instanceof CreateCatRequest createRequest) {
            Long parentId = createRequest.getParentCategoryId();
            Set<Long> subIds = request.getSubCategoriesIds();

            if (parentId != null && subIds != null && subIds.contains(parentId)) {
                throw new RuntimeException(
                        String.format("Category %d cannot be both parent and subcategory", parentId));
            }
        }
    }

    /**
     * Проверяет что подкатегории не имеют других родителей
     */
    public static void validateSubcategoryParentCheck(CategoryRequest request, CategoryService categoryService) {
        if (request.getSubCategoriesIds() == null || request.getSubCategoriesIds().isEmpty()) {
            return;
        }

        Long currentCategoryId = (request instanceof UpdateCatRequest updateRequest) ?
                updateRequest.getId() : null;

        for (Long subId : request.getSubCategoriesIds()) {
            Category subCategory = categoryService.getCategoryById(subId);
            if (subCategory == null) {
                throw new RuntimeException("Subcategory not found: " + subId);
            }

            if (subCategory.getParentCategory() != null &&
                    !subCategory.getParentCategory().getId().equals(currentCategoryId)) {
                throw new RuntimeException(
                        String.format("Category %d already has parent category %d",
                                subId, subCategory.getParentCategory().getId()));
            }
        }
    }

    /**
     * Проверяет глубокие циклические зависимости
     */
    public static void validateDeepCircularReference(CategoryRequest request, CategoryService categoryService) {
        Set<Long> visitedIds = new HashSet<>();
        Long currentId = (request instanceof UpdateCatRequest updateRequest) ?
                updateRequest.getId() : null;

        if (currentId != null) {
            visitedIds.add(currentId);
        }

        // Проверяем путь вверх через родителей
        Long parentId = null;
        if (request instanceof CreateCatRequest createRequest) {
            parentId = createRequest.getParentCategoryId();
        }

        checkParentPath(parentId, visitedIds, categoryService);

        // Проверяем путь вниз через подкатегории
        if (request.getSubCategoriesIds() != null) {
            for (Long subId : request.getSubCategoriesIds()) {
                checkSubcategoryPath(subId, new HashSet<>(visitedIds), categoryService);
            }
        }
    }

    private static void checkParentPath(Long categoryId, Set<Long> visitedIds, CategoryService categoryService) {
        while (categoryId != null) {
            if (!visitedIds.add(categoryId)) {
                throw new RuntimeException(
                        String.format("Circular dependency detected: Category %d is already in parent hierarchy path. " +
                                        "This means a category is trying to reference itself through its parent chain.",
                                categoryId));
            }
            Category parent = categoryService.getCategoryById(categoryId);
            if (parent == null) {
                break;
            }

            categoryId = parent.getParentCategory() != null ?
                    parent.getParentCategory().getId() : null;
        }
    }

    private static void checkSubcategoryPath(Long categoryId, Set<Long> visitedIds, CategoryService categoryService) {
        if (!visitedIds.add(categoryId)) {
            throw new RuntimeException(
                    String.format("Circular dependency detected: Category %d is already in subcategory hierarchy path. " +
                                    "This means a category is trying to reference itself through its subcategories chain.",
                            categoryId));
        }

        Category category = categoryService.getCategoryById(categoryId);
        if (category != null && category.getSubCategories() != null) {
            for (Category subCategory : category.getSubCategories()) {
                checkSubcategoryPath(subCategory.getId(), visitedIds, categoryService);
            }
        }
    }
}
