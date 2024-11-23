package com.eshop.client.controller;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest webRequest) {
//        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(webRequest);
        // You can log errorDetails, or process it as needed
        return "page_404"; // Return the name of your 404 error page
    }

    // Optionally, you can create a getter for the error path
    public String getErrorPath() {
        return "/error"; // You can return the error path here, but it's optional in newer versions
    }
}