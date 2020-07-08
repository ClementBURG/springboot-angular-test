package com.burgc.microcommerce.web.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.burgc.microcommerce.dao.IProductDao;
import com.burgc.microcommerce.model.Product;
import com.burgc.microcommerce.web.exceptions.NegativeIncomeMarginException;
import com.burgc.microcommerce.web.exceptions.ProductNotFoundException;

@Api("API for products CRUD operations.")
@RestController
public class ProductController {
	@Autowired
    private IProductDao productDao;
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
    public MappingJacksonValue getAllProducts() {
        List<Product> products = productDao.findAll();

        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept("buyingPrice");

        FilterProvider filtersList = new SimpleFilterProvider().addFilter("myDynamicFilter", myFilter);

        MappingJacksonValue filteredProducts = new MappingJacksonValue(products);

        filteredProducts.setFilters(filtersList);

        return filteredProducts;
    }
	
	@ApiOperation(value="Get a product by its ID.")
	@GetMapping(value="/products/{id}")
	  public Product getProductById(@PathVariable int id) throws ProductNotFoundException {
		Product product = productDao.findById(id);
		
		if (product == null) throw new ProductNotFoundException("Product with id " + id + " can not be found.");
		
	    return product;
	}
	
	@PostMapping(value="/products")
	public ResponseEntity<Void> postProduct(@Valid @RequestBody Product product) throws NegativeIncomeMarginException {
		if(product.getIncomeMargin() < 0) throw new NegativeIncomeMarginException("Product with id " + product.getId() + " has a negative income margin.");
		
		Product addedProduct = productDao.save(product);
		
		if(addedProduct == null) {
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedProduct.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(value="/products")
	public void updateProduct(@Valid @RequestBody Product product) {
		if(product.getIncomeMargin() < 0) throw new NegativeIncomeMarginException("Product with id " + product.getId() + " has a negative income margin.");
		
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
	
	@GetMapping(value="/products/calculateIncomeMargin")
    public Map<String,Float> calculateIncomeMargin() {
        Map<String, Float> incomeMargin = new HashMap<>();
        
        productDao.findAll().forEach(product -> {
        	incomeMargin.put(product.toString() , product.getIncomeMargin());
        });
        
        return incomeMargin;
    }
	
	@RequestMapping(value="/products/alphabeticalOrder", method=RequestMethod.GET)
    public MappingJacksonValue getAllProductsAlphabeticalOrder(@RequestParam(name="page", required=false, defaultValue="0") int page,
    		@RequestParam(name="size", required=false, defaultValue="5") int size) {
        Page<Product> products = productDao.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));

        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept("buyingPrice");

        FilterProvider filtersList = new SimpleFilterProvider().addFilter("myDynamicFilter", myFilter);

        MappingJacksonValue filteredProducts = new MappingJacksonValue(products);

        filteredProducts.setFilters(filtersList);

        return filteredProducts;
    }
}
