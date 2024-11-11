package com.vozh.art.dataservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity<Long> {

    private String description;

    @ManyToMany(mappedBy = "categories")

    private Set<Certificate> certificates;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory",cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = false)
    private Set<Category> subCategories;
}
