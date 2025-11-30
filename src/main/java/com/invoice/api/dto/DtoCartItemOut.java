package com.invoice.api.dto;

public class DtoCartItemOut {
    
    private Integer cart_item_id;
    private String gtin;
    private String product; 
    private Double unit_price; 
    private Integer quantity;
    private Double total_item; // Calculado (precio * cantidad)

    public DtoCartItemOut() {}
    
    // Getters y Setters
	public Integer getCart_item_id() {
		return cart_item_id;
	}

	public void setCart_item_id(Integer cart_item_id) {
		this.cart_item_id = cart_item_id;
	}

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

	public Double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotal_item() {
		return total_item;
	}

	public void setTotal_item(Double total_item) {
		this.total_item = total_item;
	}
}