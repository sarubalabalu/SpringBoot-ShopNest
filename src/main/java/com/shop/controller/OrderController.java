package com.shop.controller;

import com.shop.model.Order;
import com.shop.model.User;
import com.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private UserService userService;
    @Autowired private CartService cartService;

    private User getCurrentUser(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Authentication auth) {
        User user = getCurrentUser(auth);
        var cart = cartService.getOrCreateCart(user);
        if (cart.getItems().isEmpty()) return "redirect:/cart";
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             @RequestParam String paymentMethod,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = getCurrentUser(auth);
            Order order = orderService.placeOrder(user, shippingAddress, paymentMethod);
            return "redirect:/orders/" + order.getId() + "/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    @GetMapping("/{id}/success")
    public String orderSuccess(@PathVariable Long id, Model model, Authentication auth) {
        User user = getCurrentUser(auth);
        orderService.getById(id).ifPresent(o -> model.addAttribute("order", o));
        model.addAttribute("cartCount", 0);
        return "order-success";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, Authentication auth) {
        User user = getCurrentUser(auth);
        model.addAttribute("orders", orderService.getUserOrders(user));
        model.addAttribute("cartCount", cartService.getOrCreateCart(user).getTotalItems());
        return "my-orders";
    }
}
