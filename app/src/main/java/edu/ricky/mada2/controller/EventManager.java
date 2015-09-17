package edu.ricky.mada2.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.ricky.mada2.model.DbModel;
import edu.ricky.mada2.model.Event;
import edu.ricky.mada2.model.EventModel;
import edu.ricky.mada2.model.Invitee;

/**
 * Created by Ricky Wu on 2015/9/17.
 */
public class EventManager {
    private EventModel eModel;
    private DbModel db;
    public static EventManager singleton;
    public static EventManager getSingleton(Context context) {
        if(singleton == null) {
            singleton = new EventManager(context);
        }
        return singleton;
    }

    private EventManager(Context context) {
        eModel = EventModel.getSingleton();
        db = DbModel.getSingleton(context);

        readFromDB();
    }

    public void add(String name, Date date, String venue, String loc, String movieID, List<Invitee> invitees) {
        String id;
        Event event;
        do{
            id = createID(movieID);
        } while(eModel.contains(id));
        event = new Event(id, movieID);
        event.setName(name);
        event.setVenue(venue);
        event.setEventDate(date);
        event.setLocation(event.new Location(loc));
        event.setMovie(movieID);
        event.setInvitees((ArrayList<Invitee>) invitees);
        eModel.addEvent(event);
    }

    public boolean update(String id, String name, Date date, String venue, String loc, String movieID, List<Invitee> invitees) {
        boolean result = true;
        if(!eModel.contains(id)) {
            result = false;
        } else {
            Event event = eModel.getEventById(id);
            // Update Map with new instance
            event.setName(name);
            event.setVenue(venue);
            event.setEventDate(date);
            event.setLocation(event.new Location(loc));
            event.setMovie(movieID);
            event.setInvitees((ArrayList<Invitee>) invitees);
        }
        return result;
    }

    public void remove(String id) {
        eModel.removeEvent(id);
    }

    /*private class SyncDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                readFromDB();
            } finally {

            }
            return null;
        }
    }*/

    private void writeToDB() {
        //TODO: Save Movies in the Map into Sqlite
        db.saveAllEvents(eModel.getEventMap());
        return;
    }

    private void readFromDB() {
        //TODO: Load events from Sqlite to Map
        Map<String, String> map = db.getAllEvents();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                JSONObject jo = new JSONObject(entry.getValue());
                Event e = new Event(jo);

                eModel.addEvent(e);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    /**
     * Create EventID with random 3 digits append to MovieID
      */

    private String createID(String mID)
    {
        Random random = new Random();
        int randomInt = 1+ random.nextInt(999);
        return String.format("%s-%03d", mID, randomInt);
    }

    /**
     * release DB resource
     */
    public void close() {
        Log.e("MAD", "writeToDb");
        writeToDB();
    }

    /**
     *
     * @return Events as Arraylist
     */
    public ArrayList<Event> getList() {
        return eModel.getAllEvent();
    }
}
