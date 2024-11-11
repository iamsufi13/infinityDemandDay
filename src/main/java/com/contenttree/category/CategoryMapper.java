package com.contenttree.category;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryMapper {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;
    public static CategoryDto categoryToCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setIconImage(category.getIconPath());
        categoryDto.setBannerImage(category.getBannerPath());
        categoryDto.setSolutionSetCount(category.getSolutionSets().size());
        return categoryDto;
    }

}
