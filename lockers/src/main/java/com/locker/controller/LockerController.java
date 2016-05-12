package com.locker.controller;

import com.locker.model.LockerEntity;
import com.locker.service.LockerHistoryService;
import com.locker.service.LockerService;
import com.locker.service.SearchService;
import com.locker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Date;

/**
 * Created by randyr on 2/20/16.
 */

@Controller
public class LockerController {

    @Autowired
    LockerService lockerService;

    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

    @Autowired
    LockerHistoryService lockerHistoryService;

    @RequestMapping(value = "/locker", method = RequestMethod.GET)
    public ModelAndView mainLockerApplication() {
        return getDefaultLocker();
    }

    @RequestMapping(value = "/locker/{id}",method = RequestMethod.GET)
    public ModelAndView lockerWithId(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("view");
        LockerEntity locker = lockerService.findLockerById(id);
        model.addObject("locker", locker);
        model.addObject("user", locker.getUser());
        model.addObject("history", lockerHistoryService.findAllLimit100(id));
        return model;
    }

    @RequestMapping(value = "/setuser", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView updateLockerWithUser(@ModelAttribute("locker-id") Long id, @ModelAttribute("locker-user") String user) {
        lockerService.setUser(id, user);
        RedirectView view = new RedirectView("/locker", true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/setuserfromview", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView updateLockerWithUserView(@ModelAttribute("locker-id") Long id, @ModelAttribute("locker-user") String user) {
        lockerService.setUser(id, user);
        RedirectView view = new RedirectView("/locker/" + id, true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchLocker(@ModelAttribute("floor-dropdown") int floor, @ModelAttribute("tower-dropdown") char tower) {
        ModelAndView modelAndView = new ModelAndView("locker");
        String res = searchService.searchLocker(floor, tower);
        modelAndView.addObject("lockers", lockerService.findAll());
        modelAndView.addObject("result", res);
        return modelAndView;
    }

    @RequestMapping(value = "/setexpiration", method = RequestMethod.POST)
    public ModelAndView setExpiration(@ModelAttribute("expire") String date, @ModelAttribute("lockerid") Long id) {
        lockerService.setExpirationDate(date, id);
        RedirectView view = new RedirectView("/locker/" + id);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/editlocker", method = RequestMethod.POST)
    public ModelAndView editLocker(
            @ModelAttribute("lockerid") Long id, @ModelAttribute("locker_tower") String lockerTower,
            @ModelAttribute("locker_floor") int lockerFloor, @ModelAttribute("locker_number") int lockerNumber) {
        lockerService.editLocker(id, lockerTower, lockerFloor, lockerNumber);
        RedirectView view = new RedirectView("/locker/" + id);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    private ModelAndView getDefaultLocker() {
        ModelAndView model = new ModelAndView("locker");
        model.addObject("lockers", lockerService.findAll());
        return model;
    }
}
