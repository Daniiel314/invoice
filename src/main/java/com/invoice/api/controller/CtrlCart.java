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
@RequestMapping("/cart-item")
public class CtrlCart {

    @Autowired
    private SvcCart svc;

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@Valid @RequestBody DtoCartItemIn dtoIn) {
        return new ResponseEntity<>(svc.addToCart(dtoIn), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DtoCartItemOut>> getCart() {
        return new ResponseEntity<>(svc.getCart(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(svc.removeFromCart(id), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> clearCart() {
        return new ResponseEntity<>(svc.clearCart(), HttpStatus.OK);
    }
}