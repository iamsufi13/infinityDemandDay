package com.contenttree.solutionsets;

import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.apache.el.util.ReflectionUtil;
import org.aspectj.weaver.patterns.AndPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/solutionsets")
public class SolutionSetsController {
    @Autowired
    SolutionSetsService solutionSetsService;

    @GetMapping
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getAllSolutionSets(){
        List<SolutionSets> list = solutionSetsService.getAllSolutioinSets();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
    @GetMapping("/downloadpdf")
    public ResponseEntity<byte[]> downloadSolutionSets(@RequestParam String name){
    byte[] pdfData = solutionSetsService.downloadPdf(name);
    if (pdfData==null || pdfData.length ==0){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfData);
    }

    @PostMapping
    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSets(@RequestParam String name, @RequestParam String fileType, @RequestParam MultipartFile file){
        String uploadResponse = solutionSetsService.uploadSolutionSets(file);
        System.out.println("************************************");
        System.out.println("File Upload Successfull");
        System.out.println(uploadResponse);
        System.out.println("************************************");

        return ResponseEntity.ok(new ApiResponse1<>(true,"SUCCESS",null));


    }


}

