package com.codegnan.shopnest.Service;

import com.codegnan.shopnest.Entity.*;
import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);
    
    Category saveCategory(Category category);
    void deleteCategory(Long id);
}