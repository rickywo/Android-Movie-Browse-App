package edu.ricky.mada2.model;

import java.util.HashMap;
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
    public void addEvent(String name, Movie movie) {
        String id;
        Event event;
        do{
            id = createID(movie);
        } while(contains(id));
        event = new Event(name, movie);
        movie.addEvent(event);
        eventMap.put(event.getID(), event);
    }

    public Event getEventById(String id)
    {
        return eventMap.get(id);
    }

    public boolean removeEvent(Event event) {
        //TODO:
        // Check it exist in Map then remove it from list of movie object
        // Finally remove it from eventMap
        String id = event.getID();
        boolean result = true;
        if(!eventMap.containsKey(id)) {
            result = false;
        } else {
            Movie m = event.getMovie();
            m.removeEvent(id);
            eventMap.remove(event.getID());
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

    // Create EventID with random 3 digits append to MovieID
    private String createID(Movie m)
    {
        Random random = new Random();
        int randomInt = 1+ random.nextInt(999);
        return String.format("%s-%03d", m.getTitle(), randomInt);
    }
}
