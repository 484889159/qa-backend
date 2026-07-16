package com.qa.controller;

import com.qa.common.Result;
import com.qa.entity.Category;
import com.qa.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result getCategoryList() {
        List<Category> list = categoryService.getCategoryList();
        return Result.success(list);
    }
}