package com.eshop.client.controller;


import com.eshop.client.config.MessageConfig;
import com.eshop.client.enums.RoleType;
import com.eshop.client.model.UserModel;
import com.eshop.client.service.MailService;
import com.eshop.client.service.NotificationService;
import com.eshop.client.service.impl.UserServiceImpl;
import com.eshop.client.util.SessionHolder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.eshop.client.util.StringUtils.generateFilterKey;


@Controller
@AllArgsConstructor
public class LoginController {

    final MessageConfig messages;
    final SessionHolder sessionHolder;
    final HttpServletRequest request;
    final UserServiceImpl userService;
    final MailService mailService;
    final NotificationService notificationService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ModelAndView loadPage(@PathVariable String name) {
        if (name == null || name.isEmpty() || name.equals("favicon.ico"))
            return new ModelAndView("dashboard");

        UserModel user = sessionHolder.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView(name);
        modelAndView.addObject("currentUser", sessionHolder.getCurrentUserAsJsonString());
        String cacheKey = generateFilterKey("Notification","findAllByRecipientIdAndNotRead",user.getId(),PageRequest.of(0,10));
        modelAndView.addObject("notifications", notificationService.findAllByRecipientIdAndNotRead(user.getId(), PageRequest.of(0,10),cacheKey));
        modelAndView.addObject("fullName", user.getFirstName() + " " + user.getLastName());
        modelAndView.addObject("pageTitle", messages.getMessage(name));
        modelAndView.addObject("errorMsg", null);
        request.getParameterMap().forEach((key, value) -> {
            modelAndView.addObject(key, value[0]);
        });
        return modelAndView;
    }

    @GetMapping(value = {"", "/"})
    public String index() {
        var requestWrapper = sessionHolder.getRequestWrapper();
        String targetUrl = "/access-denied";
        if (requestWrapper.isUserInRole(RoleType.ADMIN) || requestWrapper.isUserInRole(RoleType.SUPER_WISER) || requestWrapper.isUserInRole(RoleType.USER) || requestWrapper.isUserInRole(RoleType.GUEST))
            targetUrl = "redirect:/dashboard";
        return targetUrl;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(ModelAndView modelAndView) {
        String errorMsg = request.getParameter("errorMsg");
        if (errorMsg != null)
            modelAndView.addObject("errorMsg", messages.getMessage(errorMsg));
        modelAndView.setViewName("login");
        request.getParameterMap().forEach((key, value) -> {
            modelAndView.addObject(key, value[0]);
        });
        return modelAndView;
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute("user") UserModel user) {
        userService.register(user);
        return "redirect:/login#signin";
    }

    @PostMapping("/send-OTP")
    public String changePassword(@Valid @ModelAttribute("user") UserModel user) {
        try {
            mailService.sendOTP(user.getEmail());
        } catch(Exception e) {}
        return "redirect:/login#signin";
    }

}
