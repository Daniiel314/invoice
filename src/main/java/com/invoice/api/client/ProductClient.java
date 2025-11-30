package com.invoice.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.invoice.api.dto.DtoProduct;
import com.invoice.api.dto.ApiResponse;

@FeignClient(name = "product-api")
public interface ProductClient {

    @GetMapping("/product/gtin/{gtin}")
    ResponseEntity<DtoProduct> getProduct(@PathVariable("gtin") String gtin);

    @PutMapping("/product/{gtin}/stock")
    ResponseEntity<ApiResponse> updateProductStock(@PathVariable("gtin") String gtin, @RequestBody Integer quantity);
}