package edu.ricky.mada2.model;

import org.json.JSONArray;
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
    private String invitees;

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
        this.invitees   = new JSONArray().toString();
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

        try {setInvitees(tempObject.getString(INVITEES));}
        catch (JSONException e) {
            e.printStackTrace();
        }

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

    public String getInvitees() {
        return this.invitees;
    }

    public void setInvitees(String s) {
        this.invitees = s;
    }

    public boolean removeInviteeByName(String name) {
        boolean found = false;
        try {
            JSONArray tl = new JSONArray(invitees);

            for(int i = 0 ;i <tl.length();i++) {
                if(tl.getJSONObject(i).getString(NAME).equals(name)) {
                    tl.remove(i);
                }
            }

            invitees = tl.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return found;
    }

    public boolean addInvitee(JSONObject json) {
        try {
            JSONArray tl = new JSONArray(invitees);

            for(int i = 0 ;i <tl.length();i++) {
                if(tl.getJSONObject(i).getString(NAME).equals(json.getString(NAME))) {
                    return false;
                }
            }

            tl.put(json);
            this.invitees = tl.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean addInvitee(String jsonString) {
        try {
            JSONArray tl = new JSONArray(invitees);
            JSONObject json = new JSONObject(jsonString);

            for(int i = 0 ;i <tl.length();i++) {
                if(tl.getJSONObject(i).getString(NAME).equals(json.getString(NAME))) {
                    return false;
                }
            }

            tl.put(json);
            this.invitees = tl.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
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
        valueMap.put(INVITEES, invitees);
        return new JSONObject(valueMap).toString();
    }
}
