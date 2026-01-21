package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;



public interface CategoryService {

     CategoryResponse getallcategories(Integer pageNumber , Integer pageSize,String sortBy , String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updatecategory(CategoryDTO categoryDTO,Long categoryId);

}
