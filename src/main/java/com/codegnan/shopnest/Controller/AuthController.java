package com.codegnan.shopnest.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codegnan.shopnest.Entity.User;
import com.codegnan.shopnest.Service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // -------------------------------------------------------
    // SHOW LOGIN PAGE
    // Spring Security handles the POST /login automatically
    // you only need to show the page
    // -------------------------------------------------------
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // -------------------------------------------------------
    // SHOW REGISTER PAGE
    // adds an empty User object to the model
    // so Thymeleaf can bind the form fields to it
    // -------------------------------------------------------
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // -------------------------------------------------------
    // HANDLE REGISTER FORM SUBMISSION
    // -------------------------------------------------------
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes) {

        // check if email already exists
        if (userService.emailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Email already registered. Please login."
            );
            return "redirect:/register";
        }

        // save user — password gets hashed inside UserService
        userService.registerUser(user);

        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Registration successful! Please login."
        );
        return "redirect:/login";
    }
}