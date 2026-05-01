package com.codegnan.shopnest.Service;

import java.util.List;

import com.codegnan.shopnest.Entity.Product;

public interface ProductService {

	List<Product> getAllProducts();

	List<Product> getFeaturedProducts();

	Product getProductById(Long id);

	List<Product> getProductsByCategory(Long categoryId);

	List<Product> searchProducts(String keyword);

	Product saveProduct(Product product);

	void deleteProduct(Long id);
}
