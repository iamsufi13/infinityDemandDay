package com.contenttree.category;

import com.contenttree.solutionsets.SolutionSets;
import org.apache.catalina.connector.InputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category getCategoryByName(String name){
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public Optional<Category> getCategoryBySolutionSet(long id){
        List<Category> category = categoryRepository.findAll();


        Optional<Category> matchingCategory = category.stream()
                .filter(cat -> cat.getSolutionSets() != null
                        && cat.getSolutionSets().stream()
                        .anyMatch(solution -> solution.getId() == id))
                .findFirst();


        return matchingCategory;




    }

    public Category addCategory(String name, MultipartFile icon, MultipartFile banner,String desc) {

//        String uploadDir = "src/main/resources/uploads/category/";
        String uploadDir = "/var/www/infiniteb2b/springboot/whitepapersSet";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String iconName = null;
        String bannerName = null;

        try {
            if (icon != null && !icon.isEmpty()) {
                iconName = icon.getOriginalFilename();
                Path iconPath = Paths.get(uploadDir + iconName);
                Files.write(iconPath, icon.getBytes());
            }

            if (banner != null && !banner.isEmpty()) {
                bannerName = banner.getOriginalFilename();
                Path bannerPath = Paths.get(uploadDir + bannerName);
                Files.write(bannerPath, banner.getBytes());
            }
        } catch (IOException e) {
            return null;
        }
        Category category = new Category();
        String updatedName = name.replace(" ", "-");
        category.setName(updatedName);
        category.setDescp(desc);
        String updatedIconPath = iconName != null ? iconName.replace(" ", "-") : "";
        category.setIconPath(updatedIconPath);
        String updatedBannerPath = bannerName != null ? bannerName.replace(" ", "-") : "";
        category.setBannerPath(updatedBannerPath);

        categoryRepository.save(category);
        return category;
    }


    public void saveBulkCategory(MultipartFile file) throws IOException{
        List<Category> categories = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null){
//            categories.add(new Category(line,null,null,null,null));
            Category category = new Category();
            System.out.println("+++++++++++++++++");
            System.out.println(line);
            System.out.println("+++++++++++++++++");
            category.setName(line);
            categories.add(category);

        }
        categoryRepository.saveAll(categories);
    }

    public Category updateCategory(long id, String name, MultipartFile icon, MultipartFile banner) {
        Category category = categoryRepository.findById(id).orElse(null);
        String uploadDir = "/var/www/infiniteb2b/springboot/whitepapersSet";
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
            return null;
        }

        category.setName(name);
        category.setIconPath(iconName);
        category.setBannerPath(bannerName);

        return categoryRepository.save(category);
    }
}
