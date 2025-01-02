package com.contenttree.category;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsRepository;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.user.User;
import com.contenttree.user.UserRepository;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
   public SolutionSetsService solutionSetsService;
    @Autowired
    SolutionSetsRepository solutionSetsRepository;
    @Autowired
    UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<ApiResponse1<List<CategoryDto>>> getAllCategory(
//            @RequestParam(required = false) String filter,
//            @RequestParam(required = false) String name,
//            @AuthenticationPrincipal User loggedInUser) {
//
//        List<Category> list1 = categoryService.categoryRepository.findAll();
//
//        List<CategoryDto> list = list1.stream()
//                .map(category -> CategoryMapper.categoryToCategoryDto(category, loggedInUser))
//                .collect(Collectors.toList());
//
//        if (filter != null) {
//            list = list.stream()
//                    .filter(entry -> entry.getName().toLowerCase().startsWith(filter.toLowerCase()))
//                    .collect(Collectors.toList());
//        }
//
//        if (name != null) {
//            list = list.stream()
//                    .filter(entry -> entry.getName().toLowerCase().startsWith(name.toLowerCase()))
//                    .collect(Collectors.toList());
//        }
//
//        if (list.isEmpty()) {
//            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Not Found", false));
//        }
//        System.out.println(list);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list, "SUCCESS", true));
//    }
//@GetMapping
//public ResponseEntity<ApiResponse1<List<Category>>> getAllCategory(
//        @RequestParam(required = false) String filter,
//        @RequestParam(required = false) String name,
//        @AuthenticationPrincipal User loggedInUser) {
//
//    if (loggedInUser != null) {
//        System.out.println("Logged in user: " + loggedInUser.getUsername());
//    } else {
//        System.out.println("No user logged in");
//    }
//
//    List<Category> categories = categoryService.categoryRepository.findAll();
//
//
//    if (filter != null) {
//        categories = categories.stream()
//                .filter(entry -> entry.getName().toLowerCase().startsWith(filter.toLowerCase()))
//                .toList();
//    }
//
//    if (name != null) {
//        categories = categories.stream()
//                .filter(entry -> entry.getName().toLowerCase().startsWith(name.toLowerCase()))
//                .collect(Collectors.toList());
//    }
//
//    if (categories.isEmpty()) {
//        List<Category> fallbackList = new ArrayList<>(categories);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(fallbackList, "SUCCESS", true));
//    }
//
//    return ResponseEntity.ok().body(ResponseUtils.createResponse1(categories, "SUCCESS", true));
//}


    @GetMapping("/userhomepage")
    public ResponseEntity<ApiResponse1<List<?>>> getCategoryForHomePage(@AuthenticationPrincipal User user) {
        List<Category> categoryList = categoryService.categoryRepository.findAll();

        categoryList.sort((category1, category2) -> Integer.compare(category2.getSolutionSets().size(), category1.getSolutionSets().size()));

        List<Category> updatedCategory = categoryList.stream()
                .limit(24)
                .collect(Collectors.toList());

        List<Map<String, Object>> maps = updatedCategory.stream().map(category -> {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("title", category.getName());
            categoryMap.put("imgSrc", category.getIconPath());
            categoryMap.put("description", category.getDescp());
            categoryMap.put("solutions", category.getSolutionSets().size());

            return categoryMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(maps,"SUCCESS",true));
    }

//    @GetMapping
//public ResponseEntity<ApiResponse1<List<Map<String, Object>>>> getAllCategory(
//        @RequestParam(required = false) String filter,
//        @RequestParam(required = false) String name,
//        @AuthenticationPrincipal User loggedInUser) {
//
//    if (loggedInUser != null) {
//        System.out.println("Logged in user: " + loggedInUser.getUsername());
//    } else {
//        System.out.println("No user logged in");
//    }
//
//    List<Category> categories = categoryService.categoryRepository.findAll();
//
//    if (filter != null) {
//        categories = categories.stream()
//                .filter(entry -> entry.getName().toLowerCase().startsWith(filter.toLowerCase()))
//                .toList();
//    }
//
//    if (name != null) {
//        categories = categories.stream()
//                .filter(entry -> entry.getName().toLowerCase().startsWith(name.toLowerCase()))
//                .collect(Collectors.toList());
//    }
//
//    List<Map<String, Object>> categoriesWithUrls = new ArrayList<>();
//
//
//
//    for (Category category : categories) {
//        Map<String, Object> categoryMap = new HashMap<>();
//        User user = userRepository.findById(loggedInUser.getId()).orElse(null);
//
//        assert user != null;
//        List<String> favList = user.getFavorites();
//        int isSub = 0;
//        if (favList.contains(category.getName()))
//        {
//            isSub=1;
//        }
//        categoryMap.put("id", category.getId());
//        categoryMap.put("name", category.getName());
//        categoryMap.put("iconPath", category.getIconPath());
//        categoryMap.put("bannerPath", category.getBannerPath());
//        categoryMap.put("descp", category.getDescp());
//        categoryMap.put("isSubscribe", isSub);
//
//        String categoryUrl = "https://infiniteb2b.com/category/" + category.getName().toLowerCase();
//
//        categoryMap.put("url", categoryUrl);
//
//        categoriesWithUrls.add(categoryMap);
//    }
//
//    if (categoriesWithUrls.isEmpty()) {
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(new ArrayList<>(), "SUCCESS", true));
//    }
//
//    return ResponseEntity.ok().body(ResponseUtils.createResponse1(categoriesWithUrls, "SUCCESS", true));
//}
@GetMapping
public ResponseEntity<ApiResponse1<List<Map<String, Object>>>> getAllCategory(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false) String name,
        @AuthenticationPrincipal User loggedInUser) {

    if (loggedInUser != null) {
        System.out.println("Logged in user: " + loggedInUser.getUsername());
    } else {
        System.out.println("No user logged in");
    }

    List<Category> categories = categoryService.categoryRepository.findAll();

    if (filter != null) {
        categories = categories.stream()
                .filter(entry -> entry.getName().toLowerCase().startsWith(filter.toLowerCase()))
                .toList();
    }

    if (name != null) {
        categories = categories.stream()
                .filter(entry -> entry.getName().toLowerCase().startsWith(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    List<Map<String, Object>> categoriesWithUrls = new ArrayList<>();

    if (loggedInUser != null) {
        User user = userRepository.findById(loggedInUser.getId()).orElse(null);
        assert user != null;
        List<String> favList = user.getFavorites();
        List<SolutionSets> list = solutionSetsRepository.findAll().stream().filter(solutionSets -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSets.getStatus()))).toList();


        for (Category category : categories) {
            Map<String, Object> categoryMap = new HashMap<>();
            int isSub = favList.contains(category.getName()) ? 1 : 0;
           long count= list.stream().filter(solutionSets -> solutionSets.getCategory().getId()== category.getId()).count();
            categoryMap.put("id", category.getId());
            categoryMap.put("name", category.getName());
            categoryMap.put("iconPath", "https://infiniteb2b.com/var/www/infiniteb2b/springboot/whitepapersSet/"+category.getIconPath());
            categoryMap.put("bannerPath", "https://infiniteb2b.com/var/www/infiniteb2b/springboot/whitepapersSet/"+category.getBannerPath());
            categoryMap.put("descp", category.getDescp());
            categoryMap.put("isSubscribe", isSub);
            categoryMap.put("whitePapersCount", count);


//            String categoryUrl = "https://infiniteb2b.com/category/" + category.getName().toLowerCase();
            String categoryUrl = "https://infiniteb2b.com/category/" + category.getId();
            categoryMap.put("url", categoryUrl);

            categoriesWithUrls.add(categoryMap);
        }
    } else {
        List<SolutionSets> list = solutionSetsRepository.findAll().stream().filter(solutionSets -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSets.getStatus()))).toList();
        for (Category category : categories) {
            long count= list.stream().filter(solutionSets -> solutionSets.getCategory().getId()== category.getId()).count();
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", category.getId());
            categoryMap.put("name", category.getName());
            categoryMap.put("iconPath", "https://infiniteb2b.com/var/www/infiniteb2b/springboot/whitepapersSet/"+category.getIconPath());
            categoryMap.put("bannerPath", "https://infiniteb2b.com/var/www/infiniteb2b/springboot/whitepapersSet/"+category.getBannerPath());
            categoryMap.put("descp", category.getDescp());
            categoryMap.put("isSubscribe", 0);
            categoryMap.put("whitePaperCount", count);

//            String categoryUrl = "https://infiniteb2b.com/category/" + category.getName().toLowerCase();
            String categoryUrl = "https://infiniteb2b.com/category/" + category.getId();
            categoryMap.put("url", categoryUrl);

            categoriesWithUrls.add(categoryMap);
        }
    }

    if (categoriesWithUrls.isEmpty()) {
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(new ArrayList<>(), "SUCCESS", true));
    }

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(categoriesWithUrls, "SUCCESS", true));
}
@PostMapping("/add")
    public ResponseEntity<ApiResponse1<Category>> addCategory(@RequestParam String name,
                                                              @RequestParam(required = false) MultipartFile icon,
                                                              @RequestParam(required = false) String desc,@RequestParam(required = false) MultipartFile banner){
    boolean categoryExists =  categoryService.categoryRepository.existsByName(name);
        if (categoryExists){
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Category Already Existed",true));
        }
        else {
            Category category = categoryService.addCategory(name, icon, banner,desc);
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(category, "Success", true));
        }
    }
    @Autowired
    CategoryMapper categoryMapper;

    @GetMapping("/dashboard-admin")
    public ResponseEntity<ApiResponse1<List<CategoryCountDto>>> getAllCategoryAdminDashboard() {
        List<CategoryCountDto> categoryList = categoryService.categoryRepository.findAll()
                .stream()
                .map(category -> categoryMapper.toCategoryCountDto(category))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(categoryList, "SUCCESS", true));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse1<Category>> updateCategory(@RequestParam Long id,
                                                                 @RequestParam(required = false) String name,
                                                                 @RequestParam(required = false) MultipartFile icon,
                                                                 @RequestParam(required = false) MultipartFile banner,
                                                                 @RequestParam(required = false) String desc) {

        boolean categoryExists = categoryService.categoryRepository.existsById(id);
        if (!categoryExists) {
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Category Not Found", false));
        }

        Category updatedCategory = categoryService.updateCategory(id, name, icon, banner, desc);
        if (updatedCategory == null) {
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Error updating category", false));
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedCategory, "Category Updated Successfully", true));
    }
    @PutMapping("/update/category-name")
    public ResponseEntity<String> updateCategoryName() {
        List<Category> categoryDtos = categoryService.categoryRepository.findAll();

        categoryDtos.forEach(category -> {
//            String updatedName = category.getName().replace("\"", "").replace(" ", "-");
           String updatedName = category.getName();
            category.setName(updatedName);
            System.out.println("Updated Category Name: " + updatedName);});

        return ResponseEntity.ok("Updated Name");}


    @PutMapping("/update")
    public ResponseEntity<ApiResponse1<Category>> updateCategory(@RequestParam long id,@RequestParam String name,
                                                                 @RequestParam MultipartFile icon,@RequestParam MultipartFile banner){
        Category category =categoryService.updateCategory(id,name,icon,banner);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(category,"SUCCESS",true));
    }
    @Autowired
    UserDataStorageRepository userDataStorageRepository;


    @GetMapping("/getsolution-setsbycategory/{id}")
    public ResponseEntity<ApiResponse1<Map<String, Object>>> getSolutionSets(@PathVariable long id, @AuthenticationPrincipal User user) {
        List<SolutionSets> list = solutionSetsService.getByCategoryId(id).stream().filter(solutionSets -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSets.getStatus()))).toList();

        List<Map<String, Object>> solutionSetWithStatusList = new ArrayList<>();

        boolean isUserAuthenticated = user != null;

        for (SolutionSets solutionSet : list) {
            Map<String, Object> solutionSetMap = new HashMap<>();
            solutionSetMap.put("solutionSet", solutionSet);

            if (isUserAuthenticated) {
                Optional<UserDataStorage> userDataStorage = userDataStorageRepository.findByUserIdAndSaveAndSolutionSetId(user.getId(), solutionSet.getId());
                int isSaved = (userDataStorage.isPresent() && userDataStorage.get().getSave() == 1) ? 1 : 0;
                solutionSetMap.put("isSaved", isSaved);
            } else {
                solutionSetMap.put("isSaved", 0);
            }

            solutionSetWithStatusList.add(solutionSetMap);
        }

        Category category = categoryService.categoryRepository.findById(id).orElse(null);
        String iconPath ="https://infiniteb2b.com/var/www/infiniteb2b/springboot/whitepapersSet/"+category.getIconPath();
        String bannerPath = "https://infiniteb2b.com/var/www/infiniteb2b/springboot/whitepapersSet/"+category.getBannerPath();
        category.setIconPath(iconPath);
        category.setBannerPath(bannerPath);

        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse1(null, "Category not found", false));
        }

        int isSub = 0;
        if (isUserAuthenticated) {
            List<String> userFavorites = user.getFavorites();
            if (userFavorites.contains(category.getName())) {
                isSub = 1;
            }
        }
        List<SolutionSets> list1 = solutionSetsRepository.findAll().stream().filter(solutionSets -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSets.getStatus()))).toList();
        long count = list1.stream().filter(solutionSets -> solutionSets.getCategory().getId()== category.getId()).count();

        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("isSubscribe", isSub);
        map.put("solutionSets", solutionSetWithStatusList);
        map.put("whitePapersCount", count);

        return ResponseEntity.ok()
                .body(ResponseUtils.createResponse1(map, "SUCCESS", true));
    }



    @GetMapping("/getall-solutionset")
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getAllSolutionSets(){
        List<SolutionSets> list = solutionSetsRepository.findAll().stream().filter(solutionSets -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSets.getStatus()))).toList();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
    @PostMapping("/upload-categories")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            categoryService.saveBulkCategory(file);
            return ResponseEntity.ok("Categories uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading categories: " + e.getMessage());}
    }
    @PutMapping("/update/filepath")
    public ResponseEntity<String> updateFilePath() {
        List<Category> categoryList = categoryService.categoryRepository.findAll();

        categoryList.forEach(category -> {
            String updatedIconPath = category.getIconPath() != null ? category.getIconPath().replace("-", " ") : "";
            category.setIconPath(updatedIconPath);

            String updatedBannerPath = category.getBannerPath() != null ? category.getBannerPath().replace(" ", " ") : "";
            category.setBannerPath(updatedBannerPath);
        });

        categoryService.categoryRepository.saveAll(categoryList);

        return ResponseEntity.ok("File paths updated successfully");
    }


}
