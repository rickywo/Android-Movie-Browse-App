package edu.ricky.mada2.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ricky Wu on 2015/8/26.
 */


public class Event implements Serializable {

    public static String ID = "id";
    public static String NAME = "name";
    public static String DATE = "date";
    public static String VENUE = "venue";
    public static String LOCATION = "loc";
    public static String MOVIE_ID = "movie_id";
    public static String INVITEES = "invitees";

    private String eventID, eventName, eventVenue;
    private Location eventLoc;
    private Date eventDate;
    private String movieID;
    private List<Invitee> invitees;

    public class Location {

        double latitude;
        double longitude;
        public Location(String loc) {
            String[] latlong =  loc.split(",");
            this.latitude = Double.parseDouble(latlong[0]);
            this.longitude = Double.parseDouble(latlong[1]);
        }
        public Location(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }

        @Override
        public String toString() {
            String latlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
            return latlong;
        }
    }
    /**
     * Event constructor
     * param:
     * String id: event unique id
     * String movieID: Scheduled movie id of this event
     *
     */
    public Event(String id , String movieID) {
        setID(id);
        setMovie(movieID);
        this.eventName  = null;
        this.eventLoc   = null;
        this.eventDate  = new Date();
        this.invitees   = new ArrayList<>();
    }

    /**
     * Event constructor
     * param:
     * JSONObject tempObject: Event json object
     *
     */

    public Event(JSONObject tempObject) {

        try {
            setID(tempObject.getString(ID));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setName(tempObject.getString(NAME));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setEventDate(new Date(tempObject.getString(DATE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {setVenue(tempObject.getString(VENUE));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setLocation(new Location(tempObject.getString(LOCATION)));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setMovie(tempObject.getString(MOVIE_ID));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        /*try {setInvitees(new ArrayList<Invitee>());}
        catch (JSONException e) {
            e.printStackTrace();
        }*/

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
        return (ArrayList<Invitee>)this.invitees;
    }

    public void setInvitees(ArrayList<Invitee> invitees) {
        this.invitees = invitees;
    }

    public boolean removeInviteeByName(String name) {
        boolean found = false;
        for(Invitee ivt: invitees) {
            if (ivt.getName().equals(name)) {
                this.invitees.remove(ivt);
                found =  true;
            }
        }
        return found;
    }

    public boolean addInvitee(Invitee inv) {
        boolean result = true;
        for(Invitee ivt: invitees) {
            if (ivt.getName().equals(inv.getName())) {
                result =  false;
                return result;
            } else {
                invitees.add(inv);
            }
        }
        return result;
    }

    public boolean updateInvitee(Invitee inv) {
        boolean found = false;
        for(Invitee ivt: invitees) {
            if (ivt.getName().equals(inv.getName())) {
                this.invitees.remove(ivt);
                this.invitees.add(inv);
                found =  true;
            }
        }
        return found;
    }

    private String getInviteesAsJsonString() {
        String inv_jstring = "";
        return inv_jstring;
    }

    @Override
    public String toString() {
        HashMap<String, String> valueMap = new HashMap<>();
        valueMap.put(ID, eventID);
        valueMap.put(NAME, eventName);
        valueMap.put(DATE, eventDate.toString());
        valueMap.put(VENUE, eventVenue);
        valueMap.put(LOCATION, eventLoc.toString());
        valueMap.put(MOVIE_ID, movieID);
        valueMap.put(INVITEES, getInviteesAsJsonString());
        return new JSONObject(valueMap).toString();
    }
}
