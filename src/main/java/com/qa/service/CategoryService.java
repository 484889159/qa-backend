package com.qa.service;

import com.qa.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList();
    Category getCategoryById(Integer id);
}