package com.shop.config;

import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user
        if (!userRepository.existsByEmail("admin@shopnest.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@shopnest.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        // Create sample user
        if (!userRepository.existsByEmail("user@shopnest.com")) {
            User user = new User();
            user.setName("John Doe");
            user.setEmail("user@shopnest.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("USER");
            userRepository.save(user);
        }

        // Create sample products if none exist
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                createProduct("Apple iPhone 15 Pro", "Latest iPhone with A17 Pro chip, titanium design, and pro camera system.",
                    new BigDecimal("129999"), new BigDecimal("149999"), 50, "Electronics",
                    "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400", 4.8, 324, true),

                createProduct("Samsung Galaxy S24 Ultra", "Ultimate Android flagship with S Pen, 200MP camera, and AI features.",
                    new BigDecimal("119999"), new BigDecimal("134999"), 35, "Electronics",
                    "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400", 4.7, 218, true),

                createProduct("Sony WH-1000XM5 Headphones", "Industry-leading noise cancellation with 30-hour battery life.",
                    new BigDecimal("29999"), new BigDecimal("34999"), 100, "Electronics",
                    "https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400", 4.9, 567, true),

                createProduct("MacBook Air M3", "Supercharged by M3 chip, incredibly thin and light with all-day battery.",
                    new BigDecimal("114900"), new BigDecimal("124900"), 25, "Electronics",
                    "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", 4.8, 189, true),

                createProduct("Nike Air Max 270", "Lifestyle shoes featuring the largest Air unit yet for all-day cushioning.",
                    new BigDecimal("8999"), new BigDecimal("10999"), 200, "Fashion",
                    "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", 4.5, 432, false),

                createProduct("Levi's 511 Slim Jeans", "Classic slim fit jeans with a modern feel, perfect for any occasion.",
                    new BigDecimal("3499"), new BigDecimal("4999"), 150, "Fashion",
                    "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400", 4.3, 289, false),

                createProduct("IKEA MALM Desk", "Clean-lined desk with smooth surfaces, easy to clean and keep tidy.",
                    new BigDecimal("12999"), new BigDecimal("15999"), 40, "Home & Living",
                    "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400", 4.4, 156, false),

                createProduct("Instant Pot Duo 7-in-1", "Pressure cooker, slow cooker, rice cooker, steamer, sauté, yogurt maker, and warmer.",
                    new BigDecimal("7499"), new BigDecimal("9999"), 75, "Home & Living",
                    "https://images.unsplash.com/photo-1585515320310-259814833e62?w=400", 4.6, 891, false),

                createProduct("The Alchemist - Paulo Coelho", "A magical story about following your dreams and listening to your heart.",
                    new BigDecimal("399"), new BigDecimal("499"), 500, "Books",
                    "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400", 4.7, 1204, false),

                createProduct("Yoga Mat Premium", "Non-slip, eco-friendly 6mm thick yoga mat with alignment lines.",
                    new BigDecimal("1999"), new BigDecimal("2999"), 300, "Sports",
                    "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400", 4.5, 678, false),

                createProduct("Dyson V15 Detect", "Most powerful cord-free vacuum with laser dust detection technology.",
                    new BigDecimal("52900"), new BigDecimal("59900"), 30, "Home & Living",
                    "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 4.8, 234, true),

                createProduct("Adidas Ultraboost 23", "Ultra-responsive running shoe with energy-returning BOOST cushioning.",
                    new BigDecimal("14999"), new BigDecimal("17999"), 120, "Sports",
                    "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", 4.6, 345, false)
            );
            productRepository.saveAll(products);
        }
    }

    private Product createProduct(String name, String desc, BigDecimal price, BigDecimal originalPrice,
                                   int stock, String category, String imageUrl, double rating, int reviews, boolean featured) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setOriginalPrice(originalPrice);
        p.setStock(stock);
        p.setCategory(category);
        p.setImageUrl(imageUrl);
        p.setRating(rating);
        p.setReviewCount(reviews);
        p.setIsFeatured(featured);
        return p;
    }
}
