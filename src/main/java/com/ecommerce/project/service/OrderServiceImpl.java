package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResponseNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements  OrderService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        //Getting user cart
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResponseNotFoundException("cart", "email", emailId);
        }
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseNotFoundException("address", "addressId", addressId));

        //create a new order with paymentInfo
        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Placed !!");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        //Get items from cart into the  order items
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is Empty");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);

            orderItems = orderItemRepository.saveAll(orderItems);
        }

            //Update the stock
            cart.getCartItems().forEach(item -> {
                int quantity = item.getQuantity();
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);

                //Clear the cart
                cartService.deleteProductfromCart(cart.getCartId(), item.getProduct().getProductId());

            });
            //Send back the orderSummary
            OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
            orderItems.forEach(item ->
                    orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)
                    ));
            orderDTO.setAddressId(addressId);


            return orderDTO;
        }
    }

