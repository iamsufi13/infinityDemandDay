package com.contenttree.category;

import com.contenttree.user.User;
import com.contenttree.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryMapper {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;

    public static CategoryDto categoryToCategoryDto(Category category, User loggedInUser) {
        if (category == null) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setIconImage(category.getIconPath());
        categoryDto.setBannerImage(category.getBannerPath());
        categoryDto.setSolutionSetCount(category.getSolutionSets().size());

        if (loggedInUser != null) {
            boolean isSubscribed = isCategoryInFav(category.getId(), loggedInUser);
            categoryDto.setIsSubscribe(isSubscribed ? 1 : 0);
        } else {
            categoryDto.setIsSubscribe(0);
        }

        return categoryDto;
    }

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

        categoryDto.setIsSubscribe(0);

        return categoryDto;
    }

    private static boolean isCategoryInFav(Long categoryId, User user) {
        return user != null && user.getFavorites() != null && user.getFavorites().contains(categoryId);
    }
}
