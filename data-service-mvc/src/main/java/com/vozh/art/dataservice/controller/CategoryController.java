package com.vozh.art.dataservice.controller;


import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        log.info("Creating category: {}", category);
        return ResponseEntity.ok(categoryService.createCategory(category));
    }
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
