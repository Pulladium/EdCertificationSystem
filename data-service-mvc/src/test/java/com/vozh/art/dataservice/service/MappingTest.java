package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.response.CategoryResponse;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.entity.Certificate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@TestPropertySource(locations = "classpath:application.properties")

public class MappingTest {

    @Autowired
    CertificateService certificateService;

    @Autowired
    CategoryService categoryService;

    @Test
    public void testCategoryMappingDepth(){
        Category firstCat = Category.builder()
                .id(1L)
                .description("description")
                .build();

        Category secondCat = Category.builder()
                .id(2L)
                .description("description")
                .parentCategory(firstCat)
                .build();

        Category thirdCat = Category.builder()
                .id(3L)
                .description("description")
                .parentCategory(secondCat)
                .build();

        firstCat.setSubCategories(Set.of(secondCat));
        secondCat.setParentCategory(firstCat);
        secondCat.setSubCategories(Set.of(thirdCat));
        thirdCat.setParentCategory(secondCat);

        CategoryResponse result = CategoryService.mapToResponse(firstCat, 1);
        assertTrue(result.getSubCategories().stream().anyMatch(c -> c.getSubCategories()==null));



        CategoryResponse result2 = CategoryService.mapToResponse(firstCat, 2);
        assertTrue(result2.getSubCategories().stream().anyMatch(c -> c.getSubCategories().stream().anyMatch(c2 -> c2.getSubCategories()==null)));
 }

    @Test
    public void testEmptyCertMapping() {
        CertificateResponse result = CertificateService.mapToResponse(Certificate.builder()
                .id(1L)
                .description("description")
                .build());

        assertEquals(1L, (long) result.getCertificateId());
        assertEquals("description", result.getDescription());
        assertNull(result.getIssuers());
        assertNull(result.getCategories());
    }
}
