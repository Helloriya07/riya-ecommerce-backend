package com.ecommerce.project.service;

import com.ecommerce.project.modal.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceimpl implements CategoryService {
    private final List<Category> categories = new ArrayList<>();


    @Override
    public List<Category> getallcategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        categories.add(category);

    }
}
