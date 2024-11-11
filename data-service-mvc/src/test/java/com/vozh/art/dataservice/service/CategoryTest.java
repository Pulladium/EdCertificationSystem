package com.vozh.art.dataservice.service;


import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@TestPropertySource(locations = "classpath:application.properties")
public class CategoryTest {


    @Autowired
    CategoryRepository categoryRepository;

    Category savedCat;
    @BeforeEach
    public void createTestData() {

        Category category = Category.builder()
                .description("Spring")
                .build();

        Category subCategory = Category.builder()
                .description("Spring boot")
                .parentCategory(category)
                .build();

        category.setSubCategories(Set.of(subCategory));
        subCategory.setParentCategory(category);

        Category subCategory2 = Category.builder()
                .description("Spring boot")
                .parentCategory(category)
                .build();

        category.setSubCategories(Set.of(subCategory2));
        subCategory2.setParentCategory(category);

        savedCat = categoryRepository.save(category);
    }

    @Test
    public void deletingCategorySubCategoryMustPresent() {
        Set<Category> subCategories = savedCat.getSubCategories();
        categoryRepository.deleteById(savedCat.getId());
        assertTrue(categoryRepository.findById(savedCat.getId()).isEmpty());

        for (Category subCategory : subCategories) {
            assertTrue(categoryRepository.findById(subCategory.getId()).isPresent());
        }
    }
}
