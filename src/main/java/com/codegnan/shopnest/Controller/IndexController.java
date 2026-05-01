package com.codegnan.shopnest.Controller;

import com.codegnan.shopnest.Service.CategoryService;
import com.codegnan.shopnest.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public IndexController(CategoryService categoryService,
                           ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // -------------------------------------------------------
    // HOMEPAGE
    // loads all categories for navbar and category cards
    // loads top 8 featured products
    // -------------------------------------------------------
    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("categories",
                categoryService.getAllCategories());
        model.addAttribute("featuredProducts",
                productService.getFeaturedProducts());
        return "index";
    }

    // -------------------------------------------------------
    // SEARCH
    // reads keyword from URL like /search?keyword=phone
    // returns matching products
    // -------------------------------------------------------
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam("keyword") String keyword,
            Model model) {

        model.addAttribute("products",
                productService.searchProducts(keyword));
        model.addAttribute("categories",
                categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        return "search";
    }
}