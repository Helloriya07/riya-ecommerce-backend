package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Product details
    private Long productId;
    private String image;
    @NotBlank
    @Size(min=3,message = "Product name must contain least 3 character")
    private String productName;
    @NotBlank
    @Size(min=3,message = "Product name must contain least 3 character")
    private String description;
    private Integer quantity;
    private  double price;
    private double discount;
    private double specialPrice;


    //Defining the relationship
    @ManyToOne
@JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;
}
