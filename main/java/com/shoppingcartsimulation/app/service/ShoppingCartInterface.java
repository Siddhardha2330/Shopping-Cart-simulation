package com.shoppingcartsimulation.app.service;

import java.util.List;
import java.util.Optional;

import com.shoppingcartsimulation.app.model.Cart;
import com.shoppingcartsimulation.app.model.Login;
import com.shoppingcartsimulation.app.model.Products;

public interface ShoppingCartInterface {
    void addUser(Login u);
    Optional<Login> findByUserId(Long id);
    Optional<Login> userExistsByEmail(String email);
    List<Login> findAllUsers();

    void addProduct(Products pr);
    Optional<Products> findProductById(Long productId);
    List<Products> findAllProductsById(Long productId);
    Iterable<Products> findAllProducts();

    Optional<Cart> findByCartId(Long id);
    Iterable<Cart> findAllCartsById(Long userId);
    public List<Products> findProductsByCategory(String category);
    void deleteCartById(Long id);
    void addCart(Cart cart);
    public List<String> findAllCategories();
    public void updateUser(Login user);
    boolean isProductInCart(Long loginid, Long productId);
    void updateCartQuantity(Long cartid, Integer quantity);

    
}
