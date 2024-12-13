package com.contenttree.CountryCode;

import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/country")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @GetMapping
    public ResponseEntity<ApiResponse1<List<Country>>> getAllCountry(){
        List<Country> list = countryRepository.findAll();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }

}
