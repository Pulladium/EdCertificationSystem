package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.CreateCatRequest;
import com.vozh.art.dataservice.dto.request.UpdateCatRequest;
import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.repository.CategoryRepository;
import com.vozh.art.dataservice.validate.CategoryRequestValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vozh.art.dataservice.dto.utils.CategoryMapper.mapToCategoryEntity;
import static com.vozh.art.dataservice.dto.utils.CategoryMapper.mapToResponse;

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

    public CategoryResponse updateCategory(UpdateCatRequest request){
        Category existingCat = getCategoryById(request.getId());

        if (existingCat == null) {
            throw new EntityNotFoundException("Category with id " + request.getId() + " not found");
        }

        CategoryRequestValidator.validateParentChildCircularReference(request);
        CategoryRequestValidator.validateSubcategoryParentCheck(request, this);
        CategoryRequestValidator.validateDeepCircularReference(request, this);

        Category category = mapToCategoryEntity(request);
        Category savedCategory = saveUpdateCategory(category);
        return mapToResponse(savedCategory, 2);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Set<Category> getAllCategories() {
        return categoryRepository.findAllRootCategoriesWithSubCategories();
    }

    public CategoryResponse postNewCategory(CreateCatRequest request){
        CategoryRequestValidator.validateParentChildCircularReference(request);
        CategoryRequestValidator.validateSubcategoryParentCheck(request, this);
        CategoryRequestValidator.validateDeepCircularReference(request, this);

        Category category = mapToCategoryEntity(request);
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

    public CategoryResponse makeCategoryRoot(Long id) {
        Category category = getCategoryById(id);
        if (category == null) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }

        Category parentCategory = category.getParentCategory();
        if (parentCategory == null) {
            log.warn("Category with id {} is already root category", id);
            return mapToResponse(category, 2);
        }

        parentCategory.getSubCategories().remove(category);
        category.setParentCategory(null);
        saveUpdateCategory(parentCategory);
        saveUpdateCategory(category);
        return mapToResponse(category, 2);
    }

    //add parentCategory to category
    public CategoryResponse addParentCategory(Long categoryId, Long parentCategoryId) {
        Category category = getCategoryById(categoryId);
        Category parentCategory = getCategoryById(parentCategoryId);
        if (category == null || parentCategory == null) {
            throw new EntityNotFoundException("Category with id " + categoryId + " or parent category with id " + parentCategoryId + " not found");
        }

        parentCategory.getSubCategories().add(category);
        category.setParentCategory(parentCategory);
        saveUpdateCategory(parentCategory);
        saveUpdateCategory(category);
        return mapToResponse(category, 2);
    }


    public Set<Category> getAllByIds(List<Long> ids) {
        try {
            return new HashSet<>(categoryRepository.findAllById(ids));
        } catch (Exception e) {
            throw new EntityNotFoundException("Cant find categories by ids");
        }
    }

}
