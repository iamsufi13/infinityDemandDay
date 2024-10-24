package com.contenttree.category;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsRepository;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/add")
    public ResponseEntity<ApiResponse1<Category>> addCategory(@RequestParam String name){
        Category category =categoryService.addCategory(name);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(category,"Success",true));
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
