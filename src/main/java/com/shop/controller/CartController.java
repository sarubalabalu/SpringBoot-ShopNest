package com.shop.controller;

import com.shop.model.Cart;
import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public String viewCart(Model model, Authentication auth) {
        User user = getCurrentUser(auth);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);
        cartService.addToCart(user, productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Item added to cart!");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long itemId,
                             @RequestParam int quantity,
                             Authentication auth) {
        User user = getCurrentUser(auth);
        cartService.updateQuantity(user, itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId, Authentication auth) {
        User user = getCurrentUser(auth);
        cartService.removeItem(user, itemId);
        return "redirect:/cart";
    }
}
