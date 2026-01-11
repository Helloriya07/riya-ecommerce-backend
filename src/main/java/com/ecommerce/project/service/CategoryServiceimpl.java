package com.ecommerce.project.service;

import com.ecommerce.project.modal.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceimpl implements CategoryService {

    private final List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;


    @Override
    public List<Category> getallcategories() {

        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        categories.remove(category);
        return "Category with categoryId: " + categoryId + "deleted successfully";
    }

    @Override
    public Category updatecategory(Category category,Long categoryId) {
        Optional<Category> Optionalcategory = categories.stream()
                .filter(c-> c.getCategoryId().equals(categoryId))
                .findFirst();
        if(Optionalcategory.isPresent()){
            Category existingCategory = Optionalcategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }

    }
    }
