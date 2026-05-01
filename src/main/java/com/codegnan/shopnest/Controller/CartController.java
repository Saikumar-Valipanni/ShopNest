package com.codegnan.shopnest.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.User;
import com.codegnan.shopnest.Service.CartService;
import com.codegnan.shopnest.Service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService,
                          UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // -------------------------------------------------------
    // HELPER METHOD
    // gets the currently logged in User object
    // from Spring Security context
    // used in every cart method
    // -------------------------------------------------------
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = auth.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Logged in user not found: " + email));
    }

    // -------------------------------------------------------
    // VIEW CART
    // loads cart with all items and total price
    // -------------------------------------------------------
    @GetMapping
    public String viewCart(Model model) {
        User user = getLoggedInUser();
        Cart cart = cartService.getOrCreateCart(user);

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        return "cart";
    }

    // -------------------------------------------------------
    // ADD TO CART
    // called when user clicks Add to Cart on a product
    // productId and quantity come from the form
    // -------------------------------------------------------
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes) {

        User user = getLoggedInUser();
        cartService.addToCart(user, productId, quantity);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Product added to cart!"
        );
        return "redirect:/cart";
    }

    // -------------------------------------------------------
    // UPDATE QUANTITY
    // called when user changes quantity on cart page
    // -------------------------------------------------------
    @PostMapping("/update")
    public String updateQuantity(
            @RequestParam Long cartItemId,
            @RequestParam int quantity,
            RedirectAttributes redirectAttributes) {

        cartService.updateQuantity(cartItemId, quantity);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Cart updated!"
        );
        return "redirect:/cart";
    }

    // -------------------------------------------------------
    // REMOVE ITEM
    // removes one specific product from cart
    // -------------------------------------------------------
    @PostMapping("/remove")
    public String removeItem(
            @RequestParam Long cartItemId,
            RedirectAttributes redirectAttributes) {

        cartService.removeItem(cartItemId);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Item removed from cart!"
        );
        return "redirect:/cart";
    }

    // -------------------------------------------------------
    // CLEAR CART
    // removes all items from cart at once
    // -------------------------------------------------------
    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        User user = getLoggedInUser();
        cartService.clearCart(user);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Cart cleared!"
        );
        return "redirect:/cart";
    }
}