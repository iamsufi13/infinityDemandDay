package com.contenttree.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category getCategoryByName(String name){
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public String addCategory(String name, MultipartFile icon, MultipartFile banner) {

        String uploadDir = "src/main/resources/uploads/category/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String iconName = icon.getOriginalFilename();
        String bannerName = banner.getOriginalFilename();

        try {
            Path iconPath = Paths.get(uploadDir + iconName);
            Path bannerPath = Paths.get(uploadDir + bannerName);

            Files.write(iconPath, icon.getBytes());
            Files.write(bannerPath, banner.getBytes());

        } catch (IOException e) {
            return "File Upload failed: " + e.getMessage();
        }

        Category category = new Category();
        category.setName(name);
        category.setIconPath(iconName);
        category.setBannerPath(bannerName);

        categoryRepository.save(category);

        return "Category added successfully!";
    }

}
