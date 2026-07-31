package com.shop.controller;

import com.shop.model.Cart;
import com.shop.model.User;
import com.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired private ProductService productService;
    @Autowired private UserService userService;
    @Autowired private CartService cartService;

    @GetMapping({"/", "/home"})
    public String home(Model model, Authentication auth) {
        model.addAttribute("featuredProducts", productService.getFeaturedProducts());
        model.addAttribute("allProducts", productService.getAllProducts());
        model.addAttribute("categories", productService.getAllCategories());
        addCartCount(model, auth);
        return "index";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String category,
                       @RequestParam(required = false) String search,
                       Model model, Authentication auth) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("products", productService.search(search));
            model.addAttribute("searchTerm", search);
        } else if (category != null && !category.isEmpty()) {
            model.addAttribute("products", productService.getByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        model.addAttribute("categories", productService.getAllCategories());
        addCartCount(model, auth);
        return "shop";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@org.springframework.web.bind.annotation.PathVariable Long id,
                                Model model, Authentication auth) {
        productService.getById(id).ifPresent(p -> model.addAttribute("product", p));
        model.addAttribute("relatedProducts", productService.getAllProducts().stream().limit(4).toList());
        addCartCount(model, auth);
        return "product-detail";
    }

    private void addCartCount(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            userService.findByEmail(auth.getName()).ifPresent(user -> {
                Cart cart = cartService.getOrCreateCart(user);
                model.addAttribute("cartCount", cart.getTotalItems());
            });
        } else {
            model.addAttribute("cartCount", 0);
        }
    }
}
