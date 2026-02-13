package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResponseNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
     private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
            Cart cart = createCart();
       // Retrieve Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResponseNotFoundException("product","productId",productId));
     CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
        cart.getCartId(),
        productId

);
        // Perform Validations
     if (cartItem!=null){
         throw new APIException("Product " + product.getProductName() + " already exist in cart");
     }
     if (product.getQuantity()<quantity){
         throw new APIException("Please make order of the  "+product.getProductName()
                 + "less than or equal to quantity"+ product.getQuantity()+".");
     }
     // Create Cart Item
        CartItem newCartItem = new CartItem();
     newCartItem.setProduct(product);
     newCartItem.setCart(cart);
     newCartItem.setQuantity(quantity);
     newCartItem.setDiscount(product.getDiscount());
     newCartItem.setProductPrice(product.getSpecialPrice());
        //Save cart item
     cartItemRepository.save(newCartItem);

     //reduce the quantity when product is added to cart
//     cartItem.setQuantity(product.getQuantity());

     cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice())*quantity);
     cartRepository.save(cart);
     // Return updated cart
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream =cartItems.stream().map(item ->{
                ProductDTO map = modelMapper.map(item.getProduct(),ProductDTO.class);
                map.setQuantity(item.getQuantity());
                return map;
        });
        cartDTO.setProducts(productStream.toList());
return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.size()==0){
            throw new APIException("No cart exixt");
        }
        List<CartDTO> cartDTOS = carts.stream()
        .map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p->modelMapper.map(p.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());

        return cartDTOS;
    }

    private Cart createCart(){
        Cart userCart =cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;

    }
}
