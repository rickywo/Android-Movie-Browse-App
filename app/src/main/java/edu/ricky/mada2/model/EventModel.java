package edu.ricky.mada2.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ricky Wu on 2015/9/3.
 */
public class EventModel {

    private static EventModel singleton = null;

    public static EventModel getSingleton()
    {
        if(singleton == null)
            singleton = new EventModel();
        return singleton;
    }

    // Model
    private Map<String, Event> eventMap;

    private EventModel() {
        this.eventMap = new HashMap<>();
    }

    public Map<String, Event> getEventMap() {
        return this.eventMap;
    }
    /* The procedure of add an Event
     * 1. Auto-generates an event ID
     * 2. Create an event with ID and Movie object
     * 3. Add event into Movie object's event list
     * 4. put new event into eventMap
     */
    public Event addEvent(String name, Date date, String venue, String loc, String movieID, String invitees) {
        String id;
        Event event;

        do{
            id = createID(movieID);
        } while(contains(id));
        event = new Event(id, movieID);
        event.setName(name);
        event.setVenue(venue);
        event.setEventDate(date);
        event.setLocation(event.new Location(loc));
        event.setMovie(movieID);
        //event.setInvitees(invitees);
        Log.e("EventModel", invitees);
        try {
            JSONArray tl = new JSONArray(invitees);

            for(int i = 0 ;i <tl.length();i++) {
                Log.e("EventModel", tl.getJSONObject(i).toString());
                event.addInvitee(tl.getJSONObject(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        eventMap.put(event.getID(), event);

        return event;
    }

    public String addEvent(Event event) {
        eventMap.put(event.getID(), event);
        return event.getID();
    }

    public Event getEventById(String id)
    {
        return eventMap.get(id);
    }

    public ArrayList<Event> getAllEvent() {
        return new ArrayList<>(eventMap.values());
    }

    public boolean removeEvent(String id) {
        //TODO:
        // Check it exist in Map then remove it from list of movie object
        // Finally remove it from eventMap
        boolean result = true;
        if(!eventMap.containsKey(id)) {
            result = false;
        } else {
            eventMap.remove(id);
        }
        return result;
    }

    public boolean contains(String eventID) {
        return eventMap.containsKey(eventID);
    }

    public boolean updateEvent(Event event) {
        boolean result = true;
        String id = event.getID();
        if(!eventMap.containsKey(id)) {
            result = false;
        } else {
            eventMap.remove(id);
            // Update Map with new instance
            eventMap.put(id, event);
        }
        return result;
    }

    public boolean updateEvent(Event event, String name, Date date, String venue, String loc, String movieID, String invitees) {
        boolean result = true;
        String id = event.getID();
        if(!eventMap.containsKey(id)) {
            result = false;
        } else {
            eventMap.remove(id);
            // Update Map with new instance
            event.setName(name);
            event.setVenue(venue);
            event.setEventDate(date);
            event.setLocation(event.new Location(loc));
            event.setMovie(movieID);
            event.setInvitees(invitees);
            eventMap.put(id, event);
        }
        return result;
    }

    // Create EventID with random 3 digits append to MovieID
    private String createID(String mID)
    {
        Random random = new Random();
        int randomInt = 1+ random.nextInt(999);
        return String.format("%s-%03d", mID, randomInt);
    }

    public void close() {

    }

}
