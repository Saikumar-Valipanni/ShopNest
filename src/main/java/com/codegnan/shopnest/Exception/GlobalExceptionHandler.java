package com.codegnan.shopnest.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------------------------------------
    // 404 — Page not found
    // user visits a URL that doesn't exist
    // -------------------------------------------------------
    @ExceptionHandler({
        NoHandlerFoundException.class,
        NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Exception ex, Model model) {
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage",
            "The page you are looking for doesn't exist or has been moved.");
        return "error";
    }

    // -------------------------------------------------------
    // Product / Category / Order not found
    // thrown from your service layer
    // -------------------------------------------------------
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request,
            Model model) {

        String message = ex.getMessage();

        // stock error — give specific message
        if (message != null && message.contains("Insufficient stock")) {
            model.addAttribute("errorCode", "400");
            model.addAttribute("errorTitle", "Out of Stock");
            model.addAttribute("errorMessage", message);
            return "error";
        }

        // not found errors
        if (message != null && message.contains("not found")) {
            model.addAttribute("errorCode", "404");
            model.addAttribute("errorTitle", "Not Found");
            model.addAttribute("errorMessage", message);
            return "error";
        }

        // any other runtime error
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage",
            "An unexpected error occurred. Please try again.");
        return "error";
    }

    // -------------------------------------------------------
    // 403 — Access denied
    // user tries to access admin page without ADMIN role
    // -------------------------------------------------------
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(Model model) {
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage",
            "You don't have permission to access this page.");
        return "error";
    }

    // -------------------------------------------------------
    // Catch all — anything else unexpected
    // -------------------------------------------------------
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(
            Exception ex, Model model) {
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorTitle", "Server Error");
        model.addAttribute("errorMessage",
            "Something went wrong on our end. Please try again later.");
        return "error";
    }
}