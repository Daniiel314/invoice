package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.dto.DtoCartItemOut;
import com.invoice.api.service.SvcCart;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart-item") // Prefijo base para todos los métodos
public class CtrlCart {

    @Autowired
    private SvcCart svc;

    // 1. Agregar al carrito (POST /cart-item)
    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@Valid @RequestBody DtoCartItemIn dtoIn) {
        return new ResponseEntity<>(svc.addToCart(dtoIn), HttpStatus.CREATED);
    }

    // 2. Obtener carrito (GET /cart-item/detail)
    @GetMapping({"", "/detail"}) 
    public ResponseEntity<List<DtoCartItemOut>> getCart() {
        return new ResponseEntity<>(svc.getCart(), HttpStatus.OK);
    }

    // 3. Eliminar item específico (DELETE /cart-item/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(svc.removeFromCart(id), HttpStatus.OK);
    }

    // 4. Vaciar carrito (DELETE /cart-item)
    @DeleteMapping
    public ResponseEntity<ApiResponse> clearCart() {
        return new ResponseEntity<>(svc.clearCart(), HttpStatus.OK);
    }
}