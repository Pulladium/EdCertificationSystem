package com.vozh.art.dataservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity<Long> {

    private String name;
    private String description;

    @ManyToMany(mappedBy = "categories")

    private Set<Certificate> certificates;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;


    @OneToMany(mappedBy = "parentCategory",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = false)
    private Set<Category> subCategories;


    @PreRemove
    private void removeParentFromChildren() {
        for (Category subCategory : subCategories) {
            subCategory.setParentCategory(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId()) &&
                Objects.equals(name, category.name) &&
                Objects.equals(description, category.description);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, description);
    }


}
