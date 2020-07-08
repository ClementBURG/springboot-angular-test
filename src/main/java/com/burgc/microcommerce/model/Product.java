package com.burgc.microcommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="OCTEST_PRODUCT")
// @JsonIgnoreProperties(value = {"id", "buyingPrice"})
// @JsonFilter("myDynamicFilter")
public class Product {
	@Id
	@GeneratedValue
	private int id;
	
    @Size(min=3, max=20, message="The number of characters must be between 3 and 20.")
	private String name;
    
    @Column(name="S_PRICE")
    @Min(value=1)
	private float price;
    
	private String currency;
	
	@Column(name="B_PRICE")
	// @JsonIgnore
	private float buyingPrice;
	
	public Product() {
	}

	public Product(int id, String name, float price, String currency, float buyingPrice) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.buyingPrice = buyingPrice;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public float getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(float buyingPrice) {
		this.buyingPrice = buyingPrice;
	}
	
    public float getIncomeMargin() {
    	return this.price - this.buyingPrice;
    }

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", currency=" + currency + ", buyingPrice="
				+ buyingPrice + "]";
	}
}
