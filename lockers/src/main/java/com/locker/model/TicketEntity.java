package com.locker.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by randyr on 5/10/16.
 */

@Entity
@Table(name = "lockerhistory")
public class TicketEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketid")
    public Long ticketid;

    @JoinColumn(name = "locker", referencedColumnName = "lockerid")
    @ManyToOne
    private LockerEntity locker;

    @Basic(optional = false)
    @Column(name = "ticketcontent")
    private String ticket_content;

    @Basic(optional = false)
    @Column(name = "enabled")
    private int ticket_enabled;

    public TicketEntity() {
    }

    public TicketEntity(Long ticketid) {
        this.ticketid = ticketid;
    }

    public Long getTicketid() {return ticketid;}

    public LockerEntity getLocker() {return locker;}

    public void setLocker(LockerEntity locker) {this.locker = locker;}

    public String getTicket_content() {return ticket_content;}

    public void setTicket_content(String ticket_content) {this.ticket_content = ticket_content;}

    public int getEnabled() {return ticket_enabled;}

    public void setEnabled(int enabled) {this.ticket_enabled = ticket_enabled;}

}
