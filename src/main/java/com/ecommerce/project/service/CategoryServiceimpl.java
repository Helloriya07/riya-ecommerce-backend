package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResponseNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceimpl implements CategoryService {

//    private final List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

@Autowired
private CategoryRepository categoryRepository;

    @Override
    public List<Category> getallcategories() {

        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
//        category.setCategoryId(nextId++);
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category =  categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseNotFoundException("Category","categoryId","categoryId"));


        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + "deleted successfully";
    }

    @Override
    public Category updatecategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseNotFoundException("Category","categoryId","categoryId"));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }


}

