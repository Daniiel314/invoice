package com.invoice.api.entity;

import java.time.LocalDateTime; 
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "invoice_id")
	private Integer invoice_id;

	@Column(name = "user_id")
	private Integer user_id;
	
	@Column(name = "created_at")
	private LocalDateTime created_at; 

	@Column(name = "subtotal")
	private Double subtotal;

	@Column(name = "taxes")
	private Double taxes;

	@Column(name = "total")
	private Double total;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
	private List<InvoiceItem> items;
	
	public Invoice() {
		
	}

	// Getters y Setters
	public Integer getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(Integer invoice_id) {
		this.invoice_id = invoice_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<InvoiceItem> getItems() {
		return items;
	}

	public void setItems(List<InvoiceItem> items) {
		this.items = items;
	}

}