package com.codegnan.shopnest.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codegnan.shopnest.Entity.Category;
import com.codegnan.shopnest.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findTop8ByOrderByIdAsc();

    List<Product> findByNameContainingIgnoreCase(String keyword);

	Optional<Product> findById(long id);
}