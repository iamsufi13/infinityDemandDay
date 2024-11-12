package com.contenttree.category;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsRepository;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
   public SolutionSetsService solutionSetsService;
    @Autowired
    SolutionSetsRepository solutionSetsRepository;

    @GetMapping
    public ResponseEntity<ApiResponse1<List<CategoryDto>>> getAllCategory(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String name) {

        List<Category> list1 = categoryService.categoryRepository.findAll();

        List<CategoryDto> list = list1.stream()
                .map(CategoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());

        if (filter != null) {
            list = list.stream()
                    .filter(entry -> entry.getName().toLowerCase().startsWith(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (name != null) {
            list = list.stream()
                    .filter(entry -> entry.getName().toLowerCase().startsWith(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (list.isEmpty()) {
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Not Found", false));
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list, "SUCCESS", true));
    }




    @PostMapping("/add")
    public ResponseEntity<ApiResponse1<Category>> addCategory(@RequestParam String name,
                                                              @RequestParam MultipartFile icon,@RequestParam MultipartFile banner){
        boolean categoryExists =  categoryService.categoryRepository.existsByName(name);
        if (categoryExists){
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Category Already Existed",true));
        }
        else {
            categoryService.addCategory(name, icon, banner);
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Success", true));
        }


    }
    @GetMapping("/getsolution-setsbycategory")
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getSolutionSets(@RequestParam long id){
        List<SolutionSets> list = solutionSetsService.getByCategoryId(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));

    }
    @GetMapping("/getall-solutionset")
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getAllSolutionSets(){
        List<SolutionSets> list = solutionSetsRepository.findAll();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
    @PostMapping("/upload-categories")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            categoryService.saveBulkCategory(file);
            return ResponseEntity.ok("Categories uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading categories: " + e.getMessage());
        }
    }
}
