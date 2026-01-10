package com.ecommerce.project.service;

import com.ecommerce.project.modal.Category;

import java.util.List;

public interface CategoryService {

     List<Category> getallcategories();
    void createCategory(Category category);
}
