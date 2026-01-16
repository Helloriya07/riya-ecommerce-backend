package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResponseNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CategoryServiceimpl implements CategoryService {



    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getallcategories() {
        List<Category> categories= categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No category created till now");
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null)
            throw new APIException("category with name " + category.getCategoryName() + " already exists!!");

        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseNotFoundException("Category", "categoryId", "categoryId"));


        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + "deleted successfully";
    }

    @Override
    public Category updatecategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseNotFoundException("Category", "categoryId", "categoryId"));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;


    }
}

