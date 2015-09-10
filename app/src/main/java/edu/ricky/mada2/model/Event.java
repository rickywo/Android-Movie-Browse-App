package edu.ricky.mada2.model;

import android.location.Location;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ricky Wu on 2015/8/26.
 */
public class Event implements Serializable {

    private String eventID, eventName, eventVenue;
    private Location eventLoc;
    private Date eventDate;
    private Movie eventMovie;
    private ArrayList<Invitee> invitees;

    public Event(String name , Movie movie) {
        this.eventID =
        this.eventName  = name;
        this.eventLoc   = null;
        this.eventDate  = new Date();
        this.invitees   = new ArrayList<>();
    }

    public String getID() {
        return this.eventID;
    }

    public void setID(String s) {
        this.eventID = s;
    }

    public String getName() {
        return this.eventName;
    }

    public void setName(String s) {
        this.eventName = s;
    }

    public String getVenue() {
        return this.eventVenue;
    }

    public void setVenue(String s) {
        this.eventVenue = s;
    }

    public Date getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(Date d) {
        this.eventDate = d;
    }

    public Movie getMovie() {
        return this.eventMovie;
    }

    public void setMovie(Movie m) {
        this.eventMovie = m;
    }

    public Location getLocation() {
        return this.eventLoc;
    }

    public void setLocation(Location location) {
        this.eventLoc = location;
    }

    public ArrayList<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(ArrayList<Invitee> invitees) {
        this.invitees = invitees;
    }

    public void addInvitee(Invitee invitee) {
        this.invitees.add(invitee);
    }


}
