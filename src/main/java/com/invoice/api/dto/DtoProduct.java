package com.invoice.api.dto;

public class DtoProduct {
    private String gtin;
    private String product; 
    private Integer stock;
    private Double price;
    
    // Getters y Setters
	public String getGtin() {
		return gtin;
	}
	
	public void setGtin(String gtin) {
		this.gtin = gtin;
	}
	
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}