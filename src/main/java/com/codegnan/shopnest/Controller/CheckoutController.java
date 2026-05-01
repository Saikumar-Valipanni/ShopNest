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
import com.codegnan.shopnest.Entity.Order;
import com.codegnan.shopnest.Entity.User;
import com.codegnan.shopnest.Service.CartService;
import com.codegnan.shopnest.Service.OrderService;
import com.codegnan.shopnest.Service.UserService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	private final CartService cartService;
	private final OrderService orderService;
	private final UserService userService;

	public CheckoutController(CartService cartService, OrderService orderService, UserService userService) {
		this.cartService = cartService;
		this.orderService = orderService;
		this.userService = userService;
	}

	// gets logged in user — same pattern as CartController
	private User getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
	}

	// -------------------------------------------------------
	// STEP 1 — CHECKOUT PAGE
	// shows order summary + shipping details form
	// -------------------------------------------------------
	@GetMapping
	public String showCheckoutPage(Model model, RedirectAttributes redirectAttributes) {
		User user = getLoggedInUser();
		Cart cart = cartService.getOrCreateCart(user);

		// if cart is empty redirect back
		if (cart.getCartItems().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty!");
			return "redirect:/cart";
		}

		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.getCartItems());
		model.addAttribute("cartTotal", cartService.getCartTotal(user));
		return "checkout";
	}

	// -------------------------------------------------------
	// STEP 2 — PAYMENT PAGE
	// receives shipping details, stores in session, shows
	// fake payment form
	// -------------------------------------------------------
	@PostMapping("/payment")
	public String showPaymentPage(@RequestParam String fullName, @RequestParam String phone,
			@RequestParam String address, @RequestParam String city, @RequestParam String pincode, Model model) {

		User user = getLoggedInUser();

		// pass shipping details to payment page via model
		model.addAttribute("fullName", fullName);
		model.addAttribute("phone", phone);
		model.addAttribute("address", address);
		model.addAttribute("city", city);
		model.addAttribute("pincode", pincode);
		model.addAttribute("cartTotal", cartService.getCartTotal(user));

		return "payment";
	}

	// -------------------------------------------------------
	// STEP 3 — PLACE ORDER
	// called when user clicks Pay Now
	// saves order, clears cart, redirects to success
	// -------------------------------------------------------
	@PostMapping("/place-order")
	public String placeOrder(@RequestParam String fullName, @RequestParam String phone, @RequestParam String address,
			@RequestParam String city, @RequestParam String pincode, RedirectAttributes redirectAttributes) {

		User user = getLoggedInUser();

		try {
			Order order = orderService.placeOrder(user, fullName, phone, address, city, pincode);

			redirectAttributes.addFlashAttribute("orderId", order.getId());
			redirectAttributes.addFlashAttribute("order", order);
			return "redirect:/checkout/success";

		} catch (RuntimeException e) {
			// stock insufficient — go back to cart with error
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/cart";
		}
	}

	// -------------------------------------------------------
	// STEP 4 — SUCCESS PAGE
	// -------------------------------------------------------
	@GetMapping("/success")
	public String showSuccessPage(Model model) {
		// order details come from flash attributes
		return "order-success";
	}

}