package com.invoice.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invoice.api.client.ProductClient;
import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.CartItem;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.InvoiceItem;
import com.invoice.api.repository.RepoCartItem; 
import com.invoice.api.repository.RepoInvoice;
import com.invoice.commons.mapper.MapperInvoice;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

import feign.FeignException;

@Service
public class SvcInvoiceImp implements SvcInvoice {

    @Autowired
    private RepoInvoice repo;

    @Autowired
    private RepoCartItem repoCart; 

    @Autowired
    private ProductClient productClient;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    MapperInvoice mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DtoInvoiceList> findAll() {
        try {
            if (jwtDecoder.isAdmin()) {
                return mapper.toDtoList(repo.findAll());
            } else {
                Integer user_id = jwtDecoder.getUserId();
                return mapper.toDtoList(repo.findAllByUserId(user_id)); 
            }
        } catch (DataAccessException e) {
            throw new DBAccessException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice findById(Integer id) {
        try {
            Invoice invoice = repo.findById(id).orElseThrow(() -> 
                new ApiException(HttpStatus.NOT_FOUND, "El id de la factura no existe"));
            
            if (!jwtDecoder.isAdmin()) {
                Integer user_id = jwtDecoder.getUserId();
                if (!invoice.getUser_id().equals(user_id)) {
                    throw new ApiException(HttpStatus.FORBIDDEN, "El token no es válido para consultar esta factura");
                }
            }
            return invoice;
        } catch (DataAccessException e) {
            throw new DBAccessException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse create() {
        try {
            Integer userId = jwtDecoder.getUserId();
            List<CartItem> cartItems = repoCart.findByUserId(userId);

            if (cartItems.isEmpty()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
            }

            Invoice invoice = new Invoice();
            invoice.setUser_id(userId);
            invoice.setCreated_at(LocalDateTime.now());
            
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            Double total = 0.0;
            Double taxes = 0.0;
            Double subtotal = 0.0;

            for (CartItem item : cartItems) {
                DtoProduct product;
                try {
                    ResponseEntity<DtoProduct> response = productClient.getProduct(item.getGtin());
                    product = response.getBody();
                } catch (FeignException e) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Producto no encontrado con GTIN: " + item.getGtin());
                }

                if (product.getStock() < item.getQuantity()) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Stock insuficiente para: " + product.getGtin());
                }

                productClient.updateProductStock(item.getGtin(), item.getQuantity());

                Double itemUnitPrice = product.getPrice();
                Double itemTotal = itemUnitPrice * item.getQuantity();
                Double itemTaxes = itemTotal * 0.16;
                Double itemSubtotal = itemTotal - itemTaxes;

                InvoiceItem invoiceItem = new InvoiceItem();
                invoiceItem.setGtin(item.getGtin());
                invoiceItem.setQuantity(item.getQuantity());
                invoiceItem.setUnit_price(itemUnitPrice);
                invoiceItem.setTotal(itemTotal);
                invoiceItem.setTaxes(itemTaxes);
                invoiceItem.setSubtotal(itemSubtotal);
                
                invoiceItems.add(invoiceItem);

                total += itemTotal;
                taxes += itemTaxes;
                subtotal += itemSubtotal;
            }

            invoice.setTotal(total);
            invoice.setTaxes(taxes);
            invoice.setSubtotal(subtotal);
            invoice.setItems(invoiceItems);

            repo.save(invoice);
            repoCart.deleteByUserId(userId);

            return new ApiResponse("compra realizada con éxito");

        } catch (DataAccessException e) {
            throw new DBAccessException();
        }
    }
}