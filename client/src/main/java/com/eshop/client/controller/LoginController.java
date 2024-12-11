package com.eshop.client.controller;


import com.eshop.client.config.Limited;
import com.eshop.client.config.MessageConfig;
import com.eshop.client.enums.RoleType;
import com.eshop.client.filter.SubscriptionPackageFilter;
import com.eshop.client.model.ResetPassModel;
import com.eshop.client.model.UserModel;
import com.eshop.client.service.*;
import com.eshop.client.service.impl.UserServiceImpl;
import com.eshop.client.util.SessionHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static com.eshop.client.util.StringUtils.generateFilterKey;
import static com.eshop.client.util.StringUtils.generateIdKey;


@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    final MessageConfig messages;
    final SessionHolder sessionHolder;
    final HttpServletRequest request;
    final UserServiceImpl userService;
    final MailService mailService;
    final OneTimePasswordService oneTimePasswordService;
    final NotificationService notificationService;
    final ArbitrageService arbitrageService;
    final SubscriptionPackageService subscriptionPackageService;
    final SubscriptionService subscriptionService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ModelAndView loadPage(@PathVariable String name) {
        if (name == null || name.isEmpty() || name.equals("favicon.ico"))
            return new ModelAndView("dashboard");

        UserModel user = sessionHolder.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView(name);
        if(user == null) return modelAndView;
        modelAndView.addObject("currentUser", sessionHolder.getCurrentUserAsJsonString());
        String cacheKey = generateFilterKey("Notification","findAllByRecipientIdAndNotRead",user.getId(),PageRequest.of(0,10));
        modelAndView.addObject("notifications", notificationService.findAllByRecipientIdAndNotRead(user.getId(), PageRequest.of(0,10),cacheKey));
        modelAndView.addObject("fullName", user.getFirstName() + " " + user.getLastName());
        modelAndView.addObject("pageTitle", messages.getMessage(name));
        modelAndView.addObject("errorMsg", null);
        request.getParameterMap().forEach((key, value) -> {
            modelAndView.addObject(key, value[0]);
        });
        if(name.equals("arbitrage")) {
            var limit = arbitrageService.purchaseLimit(user.getId());
            var filter = new SubscriptionPackageFilter();
            filter.setActive(true);
            PageRequest pageable = PageRequest.of(0, 100, Sort.by(Sort.Order.asc("price")));
            var subscriptionPackages = subscriptionPackageService.findAll(filter, pageable,generateFilterKey("SubscriptionPackage","findAll",filter, pageable));
            var subscription = subscriptionService.findByUserAndActivePackage(user.getId());

            modelAndView.addObject("tradeButtonText", "Trade " + limit == null ? "Now" : limit);
            modelAndView.addObject("subscription", subscription);
            modelAndView.addObject("subscriptionPackages", subscriptionPackages);
        }
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
        modelAndView.setViewName("login");
        request.getParameterMap().forEach((key, value) -> {
            modelAndView.addObject(key, value[0]);
        });
        return modelAndView;
    }

    @PostMapping("/registration")
    @Limited(requestsPerMinutes = 3)
    public String register(@Valid @ModelAttribute("user") UserModel user) {
        userService.register(user);
        return "redirect:/login#signin";
    }

    @PostMapping("/send-OTP")
    @Limited(requestsPerMinutes = 3)
    public String sendOTP(@Email @NotEmpty String email) {
        try {
            mailService.sendOTP(email,"Reset password OTP");
        } catch(Exception e) {
            return "redirect:/login?errorMsg=failedToSendEmail#send_otp";
        }
        return "redirect:/login#reset_pass";
    }

    @PostMapping("/reset-pass")
    @Limited(requestsPerMinutes = 3)
    public String changePassword(@Valid @ModelAttribute("resetPassModel") ResetPassModel resetPassModel) {
        var user = userService.findByUserNameOrEmail(resetPassModel.getLogin());
        var verify = oneTimePasswordService.verify(user.getId(), resetPassModel.getOtp());
        if (verify) {
            var newUser = new UserModel().setUserId(user.getId());
            newUser.setPassword(resetPassModel.getNewPassword());
            userService.update(user, generateIdKey("User",user.getUid().toString()),"User:*");
            return "redirect:/login#signin";
        }
        return "redirect:/login?errorMsg=invalidOTPCode#reset_pass";
    }
}
