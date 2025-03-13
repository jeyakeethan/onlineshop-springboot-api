package com.example.onlineshop.dto;

import com.example.onlineshop.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private String category;    // Name of the main category
    private String subcategory; // Name of the subcategory

    public CategoryDTO(Category category) {
        this.category = category.getCategory();
        this.subcategory = category.getSubcategory();
    }
}
