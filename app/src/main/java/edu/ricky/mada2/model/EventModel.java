package edu.ricky.mada2.model;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    // Init Firebase db reference
    private Firebase ref = new Firebase("https://crackling-heat-3830.firebaseio.com/movie-gang/events");


    private EventModel() {
        this.eventMap = new HashMap<>();
        ref.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                // TODO: Place the event which newly added to eventMap
                try {
                    String json = dataSnapshot.getValue().toString();
                    Event event = new Event(new JSONObject(json));
                    eventMap.put(event.getID(), event);
                } catch (JSONException je){
                }
                //BlogPost newPost = snapshot.getValue(BlogPost.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // TODO: Update event in eventMap if any event on firebase db is changed
                try {
                    String json = dataSnapshot.getValue().toString();
                    Event event = new Event(new JSONObject(json));
                    eventMap.remove(event.getID());
                    eventMap.put(event.getID(), event);
                } catch (JSONException je){
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO: Update event in eventMap if any event on firebase db is changed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("onDataChange", snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("onDataChange", firebaseError.toString());
            }
        });
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
    public Event addEvent(String name, Date date, String venue, String loc, String movieID, String invitees)
            throws NumberFormatException {
        // TODO: create a temp event object then pass it to createEvent
        Event event = new Event(createID(), movieID);
        event.setName(name);
        event.setVenue(venue);
        event.setEventDate(date);
        event.setLocation(event.new Location(loc));
        event.setMovie(movieID);
        try {
            JSONArray tl = new JSONArray(invitees);

            for(int i = 0 ;i <tl.length();i++) {
                Log.e("EventModel", tl.getJSONObject(i).toString());
                event.addInvitee(tl.getJSONObject(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        insertEvent(event.getID(), event);
        return event;
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

    public boolean updateEvent(Event event, String name, Date date, String venue, String loc, String movieID, String invitees)
            throws NumberFormatException {
        boolean result = true;
        String id = event.getID();
        if(!contains(id)) {
            result = false;
        } else {
            // Update Map with new instance
            event.setName(name);
            event.setVenue(venue);
            event.setEventDate(date);
            event.setLocation(event.new Location(loc));
            event.setMovie(movieID);
            event.setInvitees(invitees);
            // update event in Firebase DB
            updateEvent(event.getID(), event);
        }
        return result;
    }

    // Create EventID with random 3 digits append to MovieID
    private String createID()
    {
        String uid = UUID.randomUUID().toString();
        return uid;
    }

    public void insertEvent(String eid, Event event) {
        // Point ref to events node
        ref.child(eid).setValue(event.toString());
    }

    public void updateEvent(String eid, Event event) {
        // Point ref to events node
        Map<String, Object> eventNode = new HashMap<>();
        eventNode.put(eid, event.toString());
        ref.updateChildren(eventNode);
    }
}
