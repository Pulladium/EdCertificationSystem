package com.vozh.art.dataservice.controller;


import com.vozh.art.dataservice.dto.request.CategoryRequest;
import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/data/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        log.trace("CategoryController: Creating category: {}", request);
        return ResponseEntity.ok(categoryService.postNewCategory(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
        log.trace("CategoryController: Deleting category by id: {}", id);
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Category with id: " + id + " deleted");
    }


    @GetMapping("/byId/{id}/depth/{depth}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id, @PathVariable int depth) {
        log.trace("CategoryController: Getting category by id: {}", id);
        return ResponseEntity.ok(categoryService.getCategoryResponseById(id, depth));
    }

    @GetMapping("/all/depth/{depth}")
    public ResponseEntity<Set<CategoryResponse>> getAllCategories(@PathVariable int depth) {
        log.trace("CategoryController: Getting all categories");
        return ResponseEntity.ok(categoryService.getAllCategoriesRespWithSubCatDepth(depth));
    }
}
