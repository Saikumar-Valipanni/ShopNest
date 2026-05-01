package com.codegnan.shopnest.Controller;

import com.codegnan.shopnest.Service.CategoryService;
import com.codegnan.shopnest.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService,
                              ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // -------------------------------------------------------
    // CATEGORY PAGE
    // URL: /category/1 or /category/2 etc
    // fetches only products belonging to that category
    // -------------------------------------------------------
    @GetMapping("/category/{id}")
    public String showCategoryPage(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("category",
                categoryService.getCategoryById(id));
        model.addAttribute("products",
                productService.getProductsByCategory(id));
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "category";
    }
}