package com.contenttree.category;

import com.contenttree.user.User;
import com.contenttree.user.UserService;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDataStorageRepository userDataStorageRepository;

    public CategoryDto categoryToCategoryDto(Category category, User loggedInUser) {
        if (category == null) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setIconPath(category.getIconPath());
        categoryDto.setBannerPath(category.getBannerPath());
        categoryDto.setSolutionSetCount(category.getSolutionSets().size());

        if (loggedInUser != null) {
            boolean isSubscribed = isCategoryInFav(category.getId(), loggedInUser);
            categoryDto.setIsSubscribe(isSubscribed ? 1 : 0);
        } else {
            categoryDto.setIsSubscribe(0);
        }

        return categoryDto;
    }

    public CategoryDto categoryToCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setIconPath(category.getIconPath());
        categoryDto.setBannerPath(category.getBannerPath());
        categoryDto.setSolutionSetCount(category.getSolutionSets().size());

        categoryDto.setIsSubscribe(0);

        return categoryDto;
    }

    private boolean isCategoryInFav(Long categoryId, User user) {
        return user != null && user.getFavorites() != null && user.getFavorites().contains(categoryId);
    }

    public CategoryCountDto toCategoryCountDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryCountDto categoryCountDto = new CategoryCountDto();
        categoryCountDto.setId(category.getId());
        categoryCountDto.setName(category.getName());
        categoryCountDto.setIconPath(category.getIconPath());
        categoryCountDto.setBannerPath(category.getBannerPath());
        categoryCountDto.setDescp(category.getDescp());

        long totalDownloads = categoryDownloadedCount(category.getId());
        categoryCountDto.setTotalDownloads(totalDownloads);
        long totalSubscribers = categorySubscribedCount(category.getId());
        categoryCountDto.setTotalSubscribers(totalSubscribers);
        categoryCountDto.setTotalWhitePapers(category.getSolutionSets().size());

        return categoryCountDto;
    }

    private long categoryDownloadedCount(long categoryId) {
        List<UserDataStorage> userDataStorageList = userDataStorageRepository.findByCategoryIdList(categoryId);
        return userDataStorageList.stream().filter(userDataStorage -> userDataStorage.getDownload() == 1).count();
    }
    private long categorySubscribedCount (long categoryId){
    List<User> userList = userService.getAllUsers();
    Category category = categoryService.categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return 0;
        }

        return userList.stream()
                .filter(user -> user.getFavorites() != null && user.getFavorites().contains(category.getName()))
                .count();
    }
}
