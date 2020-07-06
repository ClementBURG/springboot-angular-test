package com.burgc.microcommerce.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.burgc.microcommerce.dao.IProductDao;
import com.burgc.microcommerce.model.Product;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class ProductController {
	@Autowired
    private IProductDao productDao;
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
    public MappingJacksonValue  getAllProducts() {
        List<Product> products = productDao.findAll();

        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept("buyingPrice");

        FilterProvider filtersList = new SimpleFilterProvider().addFilter("myDynamicFilter", myFilter);

        MappingJacksonValue filteredProducts = new MappingJacksonValue(products);

        filteredProducts.setFilters(filtersList);

        return filteredProducts;
    }
	
	@GetMapping(value = "/products/{id}")
	  public Product getProductById(@PathVariable int id) {
	    return productDao.findById(id);
	}
	
	@PostMapping(value="/products")
	public ResponseEntity<Void> postProduct(@RequestBody Product product) {
		Product addedPproduct = productDao.save(product);
		
		if(addedPproduct == null) {
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedPproduct.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(value="/products")
	public void updateProduct(@RequestBody Product product) {
		productDao.save(product);
	}
	
	@DeleteMapping(value = "/products/{id}")
	public void deleteProduct(@PathVariable int id) {
		productDao.deleteById(id);
	}
	
	@GetMapping(value="/products/priceGreaterThan/{priceLimit}")
	public List<Product> getProductsWithPriceGreaterThan(@PathVariable int priceLimit) {
		return productDao.findByPriceGreaterThan(priceLimit);
	}
	
	@GetMapping(value="/products/searchNameLike")
    public List<Product> getProductWithNameLike(@RequestParam(name="nameLike", required=true) String nameLike) {
        return productDao.findByNameLike("%" + nameLike + "%");
    }
	
	@GetMapping(value="/products/searchExpensiveProduct")
    public List<Product> getExpensiveProduct(@RequestParam(name="priceLimit", required=true) float priceLimit) {
        return productDao.searchExpensiveProduct(priceLimit);
    }
}
