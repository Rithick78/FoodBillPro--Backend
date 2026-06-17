package com.foodbillpro.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double price;

    @Column(length = 1000)
    private String description;

    // Store image as Base64 string in DB
    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    private String imageData;

    @Column(nullable = false)
    private Boolean available = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "veg_type", nullable = false)
    private VegType vegType = VegType.VEG;

    @Column(name = "is_signature", nullable = false)
    private Boolean isSignature = false;

    @Column(name = "is_fast_moving", nullable = false)
    private Boolean isFastMoving = false;

    public enum Category {
        BURGER, PIZZA, DESSERT, JUICE, SNACKS
    }

    public enum VegType {
        VEG, NON_VEG
    }
}