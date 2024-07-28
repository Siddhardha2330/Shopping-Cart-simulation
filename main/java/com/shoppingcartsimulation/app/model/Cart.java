package com.shoppingcartsimulation.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartid;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Products products;

    @ManyToOne
    @JoinColumn(name = "loginid", nullable = false)
    private Login users;

    private Integer quantity;
    private Double cost;
}
