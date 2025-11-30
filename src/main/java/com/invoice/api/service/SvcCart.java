package com.invoice.api.service;

import java.util.List;
import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.dto.DtoCartItemOut;

public interface SvcCart {
    
    List<DtoCartItemOut> getCart(); 
    
    ApiResponse addToCart(DtoCartItemIn cartItem); 
    
    ApiResponse removeFromCart(Integer cart_item_id);
    
    ApiResponse clearCart();
}