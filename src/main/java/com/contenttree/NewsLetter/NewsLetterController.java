package com.contenttree.NewsLetter;


import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/newsletter")
public class NewsLetterController {
    @Autowired
    NewsLetterService newsLetterService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse1<NewsLetter>> addNewsLetter(
            @RequestParam String name,
            @RequestParam String desc
    ){
        NewsLetter newsLetter = new NewsLetter();

        newsLetter.setName(name);
        newsLetter.setDescrption(desc);
        newsLetterService.newsLetterRepository.save(newsLetter);

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(newsLetter,"SUCCESS",true));
    }
    @GetMapping
    public ResponseEntity<ApiResponse1<List<NewsLetter>>> getAllNewsLetter(){
        List<NewsLetter> list = newsLetterService.newsLetterRepository.findAll();

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
}
