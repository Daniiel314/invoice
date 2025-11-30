package com.invoice.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.invoice.api.client.ProductClient;
import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;  
import com.invoice.api.dto.DtoCartItemOut; 
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.CartItem;
import com.invoice.api.repository.RepoCartItem; 
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;

import feign.FeignException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SvcCartImp implements SvcCart {

    @Autowired
    private RepoCartItem repo; 

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private ProductClient productClient;

    @Override
    public List<DtoCartItemOut> getCart() {
        Integer user_id = jwtDecoder.getUserId();
        List<CartItem> items = repo.findByUserId(user_id);
        List<DtoCartItemOut> outList = new ArrayList<>();
        
        for (CartItem item : items) {
            DtoCartItemOut dto = new DtoCartItemOut();
            dto.setCart_item_id(item.getCart_item_id());
            dto.setGtin(item.getGtin());
            dto.setQuantity(item.getQuantity());
            
            // Llamamos a Product API para obtener nombre y precio
            try {
                ResponseEntity<DtoProduct> response = productClient.getProduct(item.getGtin());
                DtoProduct productInfo = response.getBody();
                
                dto.setProduct(productInfo.getProduct()); 
                dto.setUnit_price(productInfo.getPrice());
                dto.setTotal_item(productInfo.getPrice() * item.getQuantity());
                
            } catch (Exception e) {
                // Si falla la comunicación, enviamos datos por defecto o null
                dto.setProduct("Información no disponible");
                dto.setUnit_price(0.0);
                dto.setTotal_item(0.0);
            }
            
            outList.add(dto);
        }
        
        return outList;
    }

    @Override
    @Transactional
    public ApiResponse addToCart(DtoCartItemIn dtoIn) { // Recibe DTO
        Integer user_id = jwtDecoder.getUserId();
        
        // Validar producto
        DtoProduct product;
        try {
            ResponseEntity<DtoProduct> response = productClient.getProduct(dtoIn.getGtin());
            product = response.getBody();
        } catch (FeignException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El producto no existe");
        }

        // Verificar existencia en carrito
        CartItem existingItem = repo.findByUserIdAndGtin(user_id, dtoIn.getGtin()).orElse(null);

        if (existingItem != null) {
            Integer newQuantity = existingItem.getQuantity() + dtoIn.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stock insuficiente. Disponible: " + product.getStock());
            }
            existingItem.setQuantity(newQuantity);
            repo.save(existingItem);
            return new ApiResponse("Cantidad actualizada en el carrito");
        } else {
            if (product.getStock() < dtoIn.getQuantity()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stock insuficiente. Disponible: " + product.getStock());
            }
            
            CartItem newItem = new CartItem();
            newItem.setUser_id(user_id);
            newItem.setGtin(dtoIn.getGtin());
            newItem.setQuantity(dtoIn.getQuantity());
            newItem.setStatus(1);
            
            repo.save(newItem);
            return new ApiResponse("Producto agregado al carrito");
        }
    }

    @Override
    @Transactional
    public ApiResponse removeFromCart(Integer cart_item_id) {
        Integer user_id = jwtDecoder.getUserId();
        CartItem item = repo.findById(cart_item_id).orElse(null);
        if (item == null || !item.getUser_id().equals(user_id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Item no encontrado");
        }
        repo.delete(item);
        return new ApiResponse("Item eliminado");
    }

    @Override
    @Transactional
    public ApiResponse clearCart() {
        Integer user_id = jwtDecoder.getUserId();
        repo.deleteByUserId(user_id);
        return new ApiResponse("Carrito vaciado");
    }
}