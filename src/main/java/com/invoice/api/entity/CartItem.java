package com.invoice.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Integer cart_item_id;

    @Column(name = "user_id")
    @NotNull
    private Integer user_id; 

    @Column(name = "gtin")
    @NotNull
    private String gtin; 

    @Column(name = "quantity")
    @NotNull
    private Integer quantity;

    @Column(name = "status")
    private Integer status; // 1: Activo, 0: Inactivo (lógica soft delete si se requiere)

    public CartItem() {
    }

    public CartItem(Integer user_id, String gtin, Integer quantity) {
        this.user_id = user_id;
        this.gtin = gtin;
        this.quantity = quantity;
        this.status = 1;
    }
    
    // Getters y Setters
	public Integer getCart_item_id() {
		return cart_item_id;
	}

	public void setCart_item_id(Integer cart_item_id) {
		this.cart_item_id = cart_item_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
    
    