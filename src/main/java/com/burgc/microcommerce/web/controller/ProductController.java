package com.burgc.microcommerce.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.burgc.microcommerce.dao.IProductDao;
import com.burgc.microcommerce.model.Product;

@RestController
public class ProductController {
	@Autowired
    private IProductDao productDao;
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }
	
	@GetMapping(value = "/products/{id}")
	  public Product getProductById(@PathVariable int id) {
	    return productDao.findById(id);
	}
	
	@PostMapping(value="/products")
	public ResponseEntity<Void> postProduct(@RequestBody Product product) {
		Product product1 = productDao.save(product);
		
		if(product == null) {
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(product1.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping(value="/products/priceGreaterThan/{priceLimit}")
	public List<Product> getProductsWithPriceGreaterThan(@PathVariable int priceLimit) {
		return productDao.findByPriceGreaterThan(priceLimit);
	}
}
