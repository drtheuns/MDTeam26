package com.locker.controller;

import com.locker.model.User;
import com.locker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by randyr on 2/12/16.
 *
 * This controller handles all the login, logout and register requests. Makes use of spring security.
 */

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    private static final Logger logger =
            LoggerFactory.getLogger(LoginController.class);


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) boolean error,
            @RequestParam(value = "logout", required = false) boolean logout,
            @RequestParam(value = "registered", required = false) boolean registered) {

        ModelAndView model = new ModelAndView();
        if (error) {
            model.addObject("error", true);
        } if (logout) {
            model.addObject("msg", true);
        } if (registered) {
            model.addObject("registered", true);
        }
        model.setViewName("login");

        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(
            @RequestParam(value = "error", required = false) boolean error) {
        ModelAndView model = new ModelAndView("register");
        if (error) {
            logger.error("Registration attempt with already taken username");
            model.addObject("error", true);
        }
        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute @Valid User user) {
        boolean result = userService.registerNewUser(user);
        return result ? "redirect:/login?registered=true" : "redirect:/register?error=true";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

}
