package com.shoppingcartsimulation.app.controller;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shoppingcartsimulation.app.model.Cart;
import com.shoppingcartsimulation.app.model.Login;
import com.shoppingcartsimulation.app.model.Products;
import com.shoppingcartsimulation.app.service.ShoppingCartImpl;

@Controller
@SessionAttributes("user")
public class ShoppingCartController {

    @Autowired
    ShoppingCartImpl s;

    @GetMapping("/signup")
    public String showSignupForm(Model m) {
        m.addAttribute("user", new Login());
        return "signupform";
    }

    @PostMapping("/createuser")
    public String createUser(@ModelAttribute Login us, Model m) {
        Optional<Login> user = s.userExistsByEmail(us.getEmail());
        if (user.isPresent()) {
            m.addAttribute("status", "User Already Exists");
        } else {
            us.setStatus("active");
            us.setLastLogin(LocalDateTime.now());
            s.addUser(us);
            m.addAttribute("status", "Signed up Successfully");
        }
        return "redirect:/loginform";
    }

    @PostMapping("/loginuser")
    public String loginUser(@ModelAttribute Login us, Model m) {
        Optional<Login> user = s.userExistsByEmail(us.getEmail());
        if (!user.isPresent()) {
            m.addAttribute("status", "User doesn't Exist");
            return "redirect:/loginform";
        } else {
            Login lo = user.get();
            if (us.getPassword().equals(lo.getPassword())) {
                if (lo.getLastLogin() != null && lo.getLastLogin().isBefore(LocalDateTime.now().minusYears(2))) {
                    lo.setStatus("inactive");
                    s.updateUser(lo);
                    m.addAttribute("status", "Account inactive due to inactivity. Please contact support.");
                    return "redirect:/loginform";
                } else {
                    lo.setLastLogin(LocalDateTime.now());
                    lo.setStatus("active");
                    s.updateUser(lo);
                    m.addAttribute("user", lo);
                    return "redirect:/productsform";
                }
            } else {
                m.addAttribute("status", "Wrong password");
                return "redirect:/loginform";
            }
        }
    }

    @GetMapping("/loginform")
    public String showLoginForm(Model m) {
        m.addAttribute("user", new Login());
        return "loginform";
    }
    @GetMapping("/homepage")
    public String showHomePage(@SessionAttribute("user") Login user,Model m) {
    	  m.addAttribute("user", user);
        return "homepage";
    }

    @GetMapping("/productsform")
    public String showProductForm(Model m, @SessionAttribute("user") Login user,
                                  @RequestParam(value = "category", required = false, defaultValue = "All") String category) {
        List<String> categories = s.findAllCategories();
        List<Products> products = s.findProductsByCategory(category);

        // Calculate total items in cart for the particular user
        Iterable<Cart> carts = s.findAllCartsById(user.getLoginid());
        int totalItems = 0;
        for (Cart cart : carts) {
            totalItems += cart.getQuantity();
        }

        Map<Long, Boolean> productInCartMap = new HashMap<>();
        for (Products product : products) {
            boolean isInCart = s.isProductInCart(user.getLoginid(), product.getProductId());
            productInCartMap.put(product.getProductId(), isInCart);
        }

        m.addAttribute("productInCartMap", productInCartMap);
        m.addAttribute("categories", categories);
        m.addAttribute("selectedCategory", category);
        m.addAttribute("products", products);
        m.addAttribute("user", user);
        m.addAttribute("totalItems", totalItems); // Add total items for the particular user to the model
        return "productsform";
    }


    @PostMapping("/addCart")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("cost") Double cost,
            @SessionAttribute("user") Login user,
            Model m) {

        Optional<Products> product = s.findProductById(productId);

        if (product.isPresent()) {
            Cart cart = new Cart();
            cart.setUsers(user);
            cart.setProducts(product.get());
            cart.setQuantity(quantity);
            cart.setCost(cost * quantity);
            s.addCart(cart);
        }

        return "redirect:/productsform";
    }

    @GetMapping("/cartspage/{id}")
    public String cartsForm(@PathVariable("id") Long userId, Model m) {
        Iterable<Cart> carts = s.findAllCartsById(userId);
        System.out.println(userId);
        System.out.println(carts);
        m.addAttribute("carts", carts);
        return "cartsform";
    }

    @GetMapping("/createproductform")
    public String showCreateProductForm(Model m) {
        m.addAttribute("product", new Products());
        return "createproductform";
    }

    @PostMapping("/createproduct")
    public String createProduct(@ModelAttribute Products p, Model m) {
        s.addProduct(p);
        return "redirect:/productsform";
    }

    @PostMapping("/deleteCart")
    public String deleteCart(
            @RequestParam("cartId") Long cartId,
            @RequestParam("userId") Long userId) {
        s.deleteCartById(cartId);
        return "redirect:/cartspage/" + userId;
    }

    @PutMapping("/updateCart")
    public ResponseEntity<Void> updateCartQuantity(
            @RequestParam("cartId") Long cartId,
            @RequestParam("quantity") Integer quantity) {
        s.updateCartQuantity(cartId, quantity);
        return ResponseEntity.ok().build();
    }
}
