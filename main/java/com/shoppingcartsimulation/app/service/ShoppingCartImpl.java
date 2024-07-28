package com.shoppingcartsimulation.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingcartsimulation.app.model.Cart;
import com.shoppingcartsimulation.app.model.Login;
import com.shoppingcartsimulation.app.model.Products;
import com.shoppingcartsimulation.app.repository.CartRepo;
import com.shoppingcartsimulation.app.repository.LoginRepo;
import com.shoppingcartsimulation.app.repository.ProductsRepo;

@Service
public class ShoppingCartImpl implements ShoppingCartInterface {
    @Autowired
    LoginRepo l;
    @Autowired
    CartRepo c;
    @Autowired
    ProductsRepo p;

    @Override
    public void addUser(Login u) {
        l.save(u);
    }

    @Override
    public Optional<Login> userExistsByEmail(String email) {
        return l.findByEmail(email);
    }

    @Override
    public Optional<Login> findByUserId(Long id) {
        return l.findById(id);
    }

    @Override
    public void addProduct(Products pr) {
        p.save(pr);
    }

    @Override
    public Optional<Products> findProductById(Long productId) {
        return p.findById(productId);
    }

    @Override
    public List<Login> findAllUsers() {
        return l.findAll();
    }

    @Override
    public Optional<Cart> findByCartId(Long id) {
        return c.findById(id);
    }

    @Override
    public Iterable<Cart> findAllCartsById(Long userId) {
        return c.findAllByUsers_Loginid(userId);
    }

    @Override
    public List<Products> findAllProductsById(Long productId) {
        return null;
    }

    @Override
    public void deleteCartById(Long id) {
        c.deleteById(id);
    }

    @Override
    public void addCart(Cart cart) {
        c.save(cart);
    }

    @Override
    public Iterable<Products> findAllProducts() {
        return p.findAll();
    }

    @Override
    public void updateUser(Login user) {
        l.save(user);
    }

    @Override
    public List<String> findAllCategories() {
        return p.findDistinctCategories();
    }

    @Override
    public List<Products> findProductsByCategory(String category) {
        if ("All".equalsIgnoreCase(category)) {
            return p.findAll();
        }
        return p.findByCategory(category);
    }

    @Override
    public boolean isProductInCart(Long loginid, Long productId) {
        return c.existsByUsers_LoginidAndProducts_ProductId(loginid, productId); // Use productId
    }
    
    @Override
    public void updateCartQuantity(Long cartId, Integer quantity) {
        Optional<Cart> cartOpt = c.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.setQuantity(quantity);
            cart.setCost(Double.parseDouble(cart.getProducts().getPrice()) * quantity); // Update cost based on new quantity
            c.save(cart);
        }
    }
}

    


