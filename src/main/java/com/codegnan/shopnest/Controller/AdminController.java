package com.codegnan.shopnest.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codegnan.shopnest.Entity.Category;
import com.codegnan.shopnest.Entity.Product;
import com.codegnan.shopnest.Service.CategoryService;
import com.codegnan.shopnest.Service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public AdminController(CategoryService categoryService,
                           ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // -------------------------------------------------------
    // DASHBOARD
    // -------------------------------------------------------
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalCategories",
                categoryService.getAllCategories().size());
        model.addAttribute("totalProducts",
                productService.getAllProducts().size());
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "admin/dashboard";
    }

    // -------------------------------------------------------
    // CATEGORIES LIST
    // -------------------------------------------------------
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "admin/categories";
    }

    // -------------------------------------------------------
    // ADD CATEGORY FORM
    // -------------------------------------------------------
    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "admin/category-form";
    }

    // -------------------------------------------------------
    // SAVE CATEGORY (add and edit)
    // -------------------------------------------------------
    @PostMapping("/categories/save")
    public String saveCategory(
            @ModelAttribute Category category,
            RedirectAttributes redirectAttributes) {
        categoryService.saveCategory(category);
        String msg = category.getId() != null
                ? "Category updated successfully!"
                : "Category added successfully!";
        redirectAttributes.addFlashAttribute("successMessage", msg);
        return "redirect:/admin/categories";
    }

    // -------------------------------------------------------
    // EDIT CATEGORY FORM
    // -------------------------------------------------------
    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("category",
                categoryService.getCategoryById(id));
        model.addAttribute("isEdit", true);
        return "admin/category-form";
    }

    // -------------------------------------------------------
    // DELETE CATEGORY
    // -------------------------------------------------------
    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Category deleted successfully!");
        return "redirect:/admin/categories";
    }

    // -------------------------------------------------------
    // PRODUCTS LIST
    // -------------------------------------------------------
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products",
                productService.getAllProducts());
        return "admin/products";
    }

    // -------------------------------------------------------
    // ADD PRODUCT FORM
    // -------------------------------------------------------
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories",
                categoryService.getAllCategories());
        model.addAttribute("isEdit", false);
        return "admin/product-form";
    }

    // -------------------------------------------------------
    // SAVE PRODUCT (add and edit)
    // -------------------------------------------------------
    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute Product product,
            @RequestParam Long categoryId,
            RedirectAttributes redirectAttributes) {

        Category category = categoryService
                .getCategoryById(categoryId);
        product.setCategory(category);
        productService.saveProduct(product);

        String msg = product.getId() != null
                ? "Product updated successfully!"
                : "Product added successfully!";
        redirectAttributes.addFlashAttribute("successMessage", msg);
        return "redirect:/admin/products";
    }

    // -------------------------------------------------------
    // EDIT PRODUCT FORM
    // -------------------------------------------------------
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("product",
                productService.getProductById(id));
        model.addAttribute("categories",
                categoryService.getAllCategories());
        model.addAttribute("isEdit", true);
        return "admin/product-form";
    }

    // -------------------------------------------------------
    // DELETE PRODUCT
    // -------------------------------------------------------
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Product deleted successfully!");
        return "redirect:/admin/products";
    }
}