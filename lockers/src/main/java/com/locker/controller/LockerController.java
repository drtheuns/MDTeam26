package com.locker.controller;

import com.locker.service.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by randyr on 2/20/16.
 */

@Controller
public class LockerController {

    @Autowired
    LockerService lockerService;

    @RequestMapping(value = "/locker", method = RequestMethod.GET)
    public ModelAndView mainLockerApplication() {
        ModelAndView model = new ModelAndView("locker");
        model.addObject("lockers", lockerService.findAll());
        return model;
    }
}
