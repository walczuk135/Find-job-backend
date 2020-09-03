package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.service.CategoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/categories/")
    public Iterable<Category> getCategories() {
        return categoryService.getAllCategory();
    }

}
