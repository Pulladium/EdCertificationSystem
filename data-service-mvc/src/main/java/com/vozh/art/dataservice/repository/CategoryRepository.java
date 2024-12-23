package com.vozh.art.dataservice.repository;

import com.vozh.art.dataservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(name = "Category.findAllRootCategoriesWithSubCategories")
    Set<Category> findAllRootCategoriesWithSubCategories();
}
