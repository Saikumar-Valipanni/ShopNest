package com.codegnan.shopnest.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.shopnest.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{
	

}
