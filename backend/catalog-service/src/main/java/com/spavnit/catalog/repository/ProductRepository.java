package com.spavnit.catalog.repository;

import com.spavnit.catalog.model.Category;
import com.spavnit.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для работы с товарами
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByIsAvailableTrue();

    List<Product> findByCategoryAndIsAvailableTrue(Category category);

    List<Product> findByNameContainingIgnoreCaseAndIsAvailableTrue(String name);

    Long countByCategory(Category category);
}