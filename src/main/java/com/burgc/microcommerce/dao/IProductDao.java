package com.burgc.microcommerce.dao;
import com.burgc.microcommerce.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDao extends JpaRepository<Product, Integer> {
	Product findById(int id);
	List<Product> findByPriceGreaterThan(float priceLimit);
	List<Product> findByNameLike(String nameLike);
	
	@Query(value="SELECT p FROM Product p WHERE p.price > :priceLimit", nativeQuery=false)
	List<Product> searchExpensiveProduct(@Param("priceLimit") float priceLimit);
}