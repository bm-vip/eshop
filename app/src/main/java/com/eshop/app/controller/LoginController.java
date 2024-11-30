package com.eshop.app.controller;


import com.eshop.app.config.MessageConfig;
import com.eshop.app.enums.RoleType;
import com.eshop.app.model.UserModel;
import com.eshop.app.util.SessionHolder;
import com.eshop.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@AllArgsConstructor
public class LoginController {

    final MessageConfig messages;
    final SessionHolder sessionHolder;
    final HttpServletRequest request;
    final UserService userService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ModelAndView loadPage(@PathVariable String name) {
        if (name == null || name.isEmpty() || name.equals("favicon.ico"))
            return new ModelAndView("dashboard");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = sessionHolder.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView(name);
        modelAndView.addObject("currentUser", sessionHolder.getCurrentUserAsJsonString());
        modelAndView.addObject("fullName", user.getFirstName() + " " + user.getLastName());
        modelAndView.addObject("pageTitle", messages.getMessage(name));
        modelAndView.addObject("errorMsg", null);
        return modelAndView;
    }

    @GetMapping(value = {"", "/"})
    public String index() {
        var requestWrapper = sessionHolder.getRequestWrapper();
        String targetUrl = "/access-denied";
        if (requestWrapper.isUserInRole(RoleType.ADMIN) || requestWrapper.isUserInRole(RoleType.SUPER_WISER))
            targetUrl = "redirect:/dashboard";
        else if (requestWrapper.isUserInRole(RoleType.USER) || requestWrapper.isUserInRole(RoleType.GUEST))
            targetUrl = "redirect:/company";
        return targetUrl;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute("userModel") UserModel userModel) {
        userService.register(userModel);
        return "redirect:/login#signin";
    }

}
