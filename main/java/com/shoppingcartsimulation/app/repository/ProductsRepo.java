package com.shoppingcartsimulation.app.repository;

import java.util.List;
import com.shoppingcartsimulation.app.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {
    
    @Query("SELECT DISTINCT p.category FROM Products p")
    List<String> findDistinctCategories();

    List<Products> findByCategory(String category);
}
