package com.eshop.app.controller;


import com.eshop.app.enums.RoleType;
import com.eshop.app.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.eshop.app.config.MessageConfig;
import com.eshop.app.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@AllArgsConstructor
public class LoginController {

    final MessageConfig messages;
    final HttpServletRequest request;
    final UserServiceImpl userService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ModelAndView loadPage(@PathVariable String name) {
        if (name == null || name.isEmpty() || name.equals("favicon.ico"))
            return new ModelAndView("dashboard");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = userService.findByUserName(auth.getName());
        ModelAndView modelAndView = new ModelAndView(name);
        modelAndView.addObject("fullName", user.getFirstName() + " " + user.getLastName());
        modelAndView.addObject("pageTitle", messages.getMessage(name));
        modelAndView.addObject("errorMsg", null);
        return modelAndView;
    }

    @GetMapping(value = {"", "/"})
    public String index() {
        SecurityContextHolderAwareRequestWrapper requestWrapper = new SecurityContextHolderAwareRequestWrapper(request, "");
        String targetUrl = "/access-denied";
        if (requestWrapper.isUserInRole(RoleType.ADMIN) || requestWrapper.isUserInRole(RoleType.SUPER_WISER))
            targetUrl = "redirect:/dashboard";
        else if (requestWrapper.isUserInRole(RoleType.USER) || requestWrapper.isUserInRole(RoleType.GUEST))
            targetUrl = "redirect:/company";
        return targetUrl;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String errorMsg = request.getParameter("errorMsg");
        if (errorMsg != null)
            modelAndView.addObject("errorMsg", messages.getMessage(errorMsg));
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute("userModel") UserModel userModel) {
        userService.register(userModel);
        return "redirect:/login#signin";
    }

}
