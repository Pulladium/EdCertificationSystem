package com.vozh.art.dataservice.controller;


import com.vozh.art.dataservice.dto.request.CategoryRequest;
import com.vozh.art.dataservice.dto.request.CreateCatRequest;
import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/data/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCatRequest request) {
        log.trace("CategoryController: Creating category: {}", request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current user authorities: {}",
                auth.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))
        );
        return ResponseEntity.ok(categoryService.postNewCategory(request));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
        log.trace("CategoryController: Deleting category by id: {}", id);
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Category with id: " + id + " deleted");
    }



    //allready have update logic in create method


//    @PreAuthorize("hasRole('ROLE_admin')")
//    @PutMapping("/update")
//    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest request) {
//        log.trace("CategoryController: Updating category: {}", request);
//        CategoryResponse response = categoryService.updateCategory(request);
//        return ResponseEntity.ok(response);
//    }



//    for every one
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
