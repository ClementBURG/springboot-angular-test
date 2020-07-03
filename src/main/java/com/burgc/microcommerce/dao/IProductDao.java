package com.burgc.microcommerce.dao;
import com.burgc.microcommerce.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDao extends JpaRepository<Product, Integer> {
	Product findById(int id);
	List<Product> findByPriceGreaterThan(float priceLimit);
}