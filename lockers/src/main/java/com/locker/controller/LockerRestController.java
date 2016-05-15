package com.locker.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locker.jsonview.Views;
import com.locker.model.AjaxResponseBody;
import com.locker.model.LockerEntity;
import com.locker.service.LockerHistoryService;
import com.locker.service.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by randyr on 5/15/16.
 */

@RestController
public class LockerRestController {

    @Autowired
    private LockerService lockerService;

    @JsonView(Views.Public.class)
    @RequestMapping(value = "/addlocker")
    public AjaxResponseBody addLocker(@RequestBody LockerEntity locker) {

        AjaxResponseBody result = new AjaxResponseBody();

        if (isValidLocker(locker)) {
            Iterable<LockerEntity> existingLockers = lockerService.checkExistingLocker(locker.getLockerTower(), locker.getLockerFloor(), locker.getLockerNumber());
            for (LockerEntity lockers : existingLockers) {
                System.out.println("EXISTINGLOCKERS: " + lockers.toString() + "\t" + existingLockers.iterator().hasNext());
            }
            if (existingLockers.iterator().hasNext()) {
                result.setCode("204");
                result.setMessage("Locker with these properties already exist.");
            } else {
                lockerService.save(locker);
                result.setCode("200");
                result.setMessage("");
            }
        } else {
            result.setCode("400");
            result.setMessage("Given locker properties are not correct.");
        }

        return result;
    }

    private boolean isValidLocker(LockerEntity locker) {
        boolean valid = true;

        if (locker == null) return false;
        //LockerFloor is already taken care of by @Min(0) constraint. If floor < 0, ajax request will return error with result == null.
        if (locker.getLockerTower().isEmpty() || locker.getLockerNumber().isEmpty()) {
            valid = false;
        }

        return valid;
    }

}
