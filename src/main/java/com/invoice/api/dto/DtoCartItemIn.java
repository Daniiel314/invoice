package com.invoice.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DtoCartItemIn {
    
    @NotNull(message = "El GTIN es requerido")
    @Pattern(regexp = "\\d{12,13}", message = "El GTIN debe tener 12 o 13 dígitos numéricos") 
    private String gtin;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;
    
	// Getters y Setters
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

    
    
}