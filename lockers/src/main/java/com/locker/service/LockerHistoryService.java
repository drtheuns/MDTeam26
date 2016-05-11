package com.locker.service;

import com.locker.dao.LockerHistoryRepository;
import com.locker.model.LockerEntity;
import com.locker.model.LockerHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Created by randyr on 5/10/16.
 */

@Service
@Transactional
public class LockerHistoryService {

    private static final String ACTION_USER_ASSIGNED = "New user was assigned";
    private static final String ACTION_LOCKER_CLEARED = "User was removed from locker";
    private static final String ACTION_EXPIRATIONDATE = "Expiration date set";
    private static final String ACTION_EXPIRATIONDATE_CLEARED = "Expiration date removed";

    @Autowired
    private LockerHistoryRepository lockerDao;

    public void logAssigned(LockerEntity locker) {
        LockerHistoryEntity his = new LockerHistoryEntity();
        his.setLocker(locker);
        his.setUser(locker.getUser());
        his.setTimestamp(locker.getTimestamp());
        his.setDate(locker.getDate());
        his.setAction(ACTION_USER_ASSIGNED);
        his.setDate_updated(now());
        lockerDao.save(his);
    }

    public void logRemoved(LockerEntity locker) {
        LockerHistoryEntity his = new LockerHistoryEntity();
        his.setLocker(locker);
        his.setUser(null);
        his.setTimestamp(locker.getTimestamp());
        his.setDate(locker.getDate());
        his.setAction(ACTION_LOCKER_CLEARED);
        his.setDate_updated(now());
        lockerDao.save(his);
    }

    public void logExpiration(LockerEntity locker) {
        LockerHistoryEntity his = new LockerHistoryEntity();
        his.setLocker(locker);
        his.setUser(locker.getUser());
        his.setDate(locker.getDate());
        his.setTimestamp(locker.getTimestamp());
        his.setAction(ACTION_EXPIRATIONDATE);
        his.setDate_updated(now());
        lockerDao.save(his);
    }

    public void logExpirationCleared(LockerEntity locker) {
        LockerHistoryEntity his = new LockerHistoryEntity();
        his.setLocker(locker);
        his.setUser(locker.getUser());
        his.setDate(null);
        his.setTimestamp(locker.getTimestamp());
        his.setAction(ACTION_EXPIRATIONDATE_CLEARED);
        his.setDate_updated(now());
        lockerDao.save(his);
    }

    private Timestamp now() {
        return new Timestamp(new java.util.Date().getTime());
    }

    public Iterable<LockerHistoryEntity> findAll() {return lockerDao.findAll();}

    public Iterable<LockerHistoryEntity> findAllLimit100(Long id) {
        return lockerDao.findAllLimit100(id);
    }
}
