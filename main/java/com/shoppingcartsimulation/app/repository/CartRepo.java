package com.shoppingcartsimulation.app.repository;

import java.util.List;
import com.shoppingcartsimulation.app.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    
    List<Cart> findAllByUsers_Loginid(Long userId);

    boolean existsByUsers_LoginidAndProducts_ProductId(Long userId, Long productId); // Use productId
}
