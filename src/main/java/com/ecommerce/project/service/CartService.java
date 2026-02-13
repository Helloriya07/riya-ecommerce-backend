package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface CartService {


    public CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
