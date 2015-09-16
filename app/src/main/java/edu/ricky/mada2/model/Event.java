package edu.ricky.mada2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ricky Wu on 2015/8/26.
 */


public class Event implements Serializable {

    private String eventID, eventName, eventVenue;
    private Location eventLoc;
    private Date eventDate;
    private String movieID;
    private Map<String ,Invitee> invitees;

    public class Location {

        double latitude;
        double longitude;
        Location(String loc) {
            String[] latlong =  loc.split(",");
            this.latitude = Double.parseDouble(latlong[0]);
            this.longitude = Double.parseDouble(latlong[1]);
        }
        Location(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }

        @Override
        public String toString() {
            String latlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
            return latlong;
        }
    }

    public Event(String id , String movieID) {
        setID(id);
        setMovie(movieID);
        this.eventName  = null;
        this.eventLoc   = null;
        this.eventDate  = new Date();
        this.invitees   = new HashMap<>();
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

    public String getMovieID() {
        return this.movieID;
    }

    public void setMovie(String movieID) {
        this.movieID = movieID;
    }

    public Location getLocation() {
        return this.eventLoc;
    }

    public void setLocation(Location loc) {
        this.eventLoc = loc;
    }

    public ArrayList<Invitee> getInvitees() {
        return new ArrayList<>(invitees.values());
    }

    public void setInvitees(ArrayList<Invitee> invitees) {
        for(Invitee inv: invitees) {
            this.invitees.put(inv.getName(), inv);
        }
    }

    public boolean removeInviteeByName(String name) {
        if(this.invitees.containsKey(name)) {
            this.invitees.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public boolean addInvitee(Invitee inv) {
        if(this.invitees.containsKey(inv.getName())) {
            return false;
        } else {
            this.invitees.put(inv.getName(), inv);
            return true;
        }
    }

    public boolean updateInvitee(Invitee inv) {
        if(this.invitees.containsKey(inv.getName())) {
            this.invitees.remove(inv.getName());
            this.invitees.put(inv.getName(), inv);
            return true;
        } else {
            return false;
        }
    }
}
