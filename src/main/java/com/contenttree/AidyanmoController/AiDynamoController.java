package com.contenttree.AidyanmoController;

import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.category.CategoryService;
import com.contenttree.user.User;
import com.contenttree.user.UserRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class AiDynamoController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/get-all-subscribers-by-id/{id}")
    public ResponseEntity<ApiResponse1<List<User>>> getAllUsersByFavoriteCategory(@PathVariable long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(ResponseUtils.createResponse1(null, "Category not found", false));
        }

        String categoryName = category.getName().toLowerCase();

        List<User> userList = userRepository.findAll().stream()
                .filter(user -> user.getFavorites().stream()
                        .anyMatch(fav -> fav.toLowerCase().equals(categoryName)))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(userList, "SUCCESS", true));
    }

}
