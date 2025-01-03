package com.contenttree.Blogs;


import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import jakarta.mail.util.LineOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogsController {
    @Autowired
    BlogsCategoryRepository blogsCategoryRepository;

    @Autowired
    BlogsRepository blogsRepository;

    @PostMapping("/add-blogs-category")
    public ResponseEntity<ApiResponse1<BlogsCategory>> addBlogscategory(@RequestParam String name, @RequestParam String blogCategoryDescp)
    {
        BlogsCategory blogsCategory = new BlogsCategory();
        blogsCategory.setBlogCategoryName(name);
        blogsCategory.setBlogCategoryDescp(blogCategoryDescp);

        blogsCategoryRepository.save(blogsCategory);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(blogsCategory,"SUCCESS",true));
    }
    @GetMapping("/get-blogs-category")
    public ResponseEntity<ApiResponse1<List<BlogsCategory>>> getAllBlogsCategory(){
        List<BlogsCategory> list = blogsCategoryRepository.findAll();

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
    @GetMapping("/get-blogs")
    public ResponseEntity<ApiResponse1<List<Blogs>>> getAllBlogs() {
        String basePath = "https://infiniteb2b.com/var/www/infiniteb2b/springboot/blogs/";

        List<Blogs> list = blogsRepository.findAll().stream().map(blog -> {
            if (blog.getBlogImage() != null && !blog.getBlogImage().isEmpty()) {
                blog.setBlogImage(basePath + blog.getBlogImage());
            }
            if (blog.getBlogPath() != null && !blog.getBlogPath().isEmpty()) {
                blog.setBlogPath(basePath + blog.getBlogPath());
            }
            return blog;
        }).toList();

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list, "SUCCESS", true));
    }


    //    @PostMapping("/add-blogs")
//    public ResponseEntity<ApiResponse1<Blogs>> addBlogs(
//            @RequestParam String name,
//            @RequestParam MultipartFile image,
//            @RequestParam MultipartFile file,@RequestParam long blogsCategoryId) {
//
//        String uploadDir = "/var/www/infiniteb2b/springboot/blogs/";
//        File directory = new File(uploadDir);
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String fileName = null;
//
//        try {
//            if (file != null && !file.isEmpty()) {
//                fileName = file.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir + fileName);
//                Files.write(filePath, file.getBytes());
//            }
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseUtils.createResponse1(null, "File upload failed: " + e.getMessage(),false));
//        }
//
//        BlogsCategory blogsCategory = blogsCategoryRepository.findById(blogsCategoryId).orElse(null);
//        Blogs blogs = new Blogs();
//        blogs.setBlogName(name);
//        blogs.setBlogsCategory(blogsCategory);
//        blogs.setBlogPath(fileName != null ? fileName : "");
//
//        blogsRepository.save(blogs);
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(blogs,"SUCCESS",true));
//    }
@PostMapping("/add-blogs")
public ResponseEntity<ApiResponse1<Blogs>> addBlogs(
        @RequestParam String name,
        @RequestParam MultipartFile image,
        @RequestParam MultipartFile file,
        @RequestParam long blogsCategoryId) {

    String uploadDir = "/var/www/infiniteb2b/springboot/blogs/";
    File directory = new File(uploadDir);

    if (!directory.exists()) {
        directory.mkdirs();
    }

    String fileName = null;
    String imageName = null;

    try {
        if (file != null && !file.isEmpty()) {
            fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + "files/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        }

        if (image != null && !image.isEmpty()) {
            imageName = image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + "images/" + imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());
        }
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.createResponse1(null, "File or image upload failed: " + e.getMessage(), false));
    }

    BlogsCategory blogsCategory = blogsCategoryRepository.findById(blogsCategoryId).orElse(null);

    if (blogsCategory == null) {
        return ResponseEntity.badRequest()
                .body(ResponseUtils.createResponse1(null, "Invalid Blogs Category ID", false));
    }

    Blogs blogs = new Blogs();
    blogs.setBlogName(name);
    blogs.setBlogsCategory(blogsCategory);
    blogs.setBlogPath(fileName != null ? "files/" + fileName : "");
    blogs.setBlogImage(imageName != null ? "images/" + imageName : "");
    blogsRepository.save(blogs);

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(blogs, "SUCCESS", true));
}


}
