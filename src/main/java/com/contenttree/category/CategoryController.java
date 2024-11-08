package com.contenttree.category;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsRepository;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/")
    public ResponseEntity<ApiResponse1<List<Category>>> getAllCategory(){
        List<Category> list = categoryService.categoryRepository.findAll();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS", true));
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
}
