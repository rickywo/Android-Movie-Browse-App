package edu.ricky.mada2.model;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky Wu on 2015/8/26.
 */
public class Movie implements Serializable {

    public static String TITLE = "Title";
    public static String YEAR = "Year";
    public static String GENRES = "Genre";
    public static String POSTER = "Poster";
    public static String PLOT = "Plot";
    public static String RATED = "Rated";
    public static String RUNTIME = "Runtime";
    public static String LANG = "Language";
    public static String DIRECTOR = "Director";
    public static String ACTORS = "Actors";
    public static String IMDB_RATING = "imdbRating";
    public static String IMDB_ID = "imdbID";
    public static String MY_RATING = "myRating";

    // General information from IMDB

    private String movieTitle;
    private String movieYear;
    private String movieGenre;
    private String movieRated;
    private String movieRuntime;
    private String movieLang;
    private String movieDirector;
    private String movieActors;
    private String movieImageURL;
    private String moviePlot;
    private String movieImdbRating;
    private String movieImdbId;

    // self-defined variablea
    private float movieMyRating; // Personal rating of this movie
    // List for holding event
    List<Event> movieEvents;

    public Movie(JSONObject tempObject) {
        try {
            setTitle(tempObject.getString(TITLE));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setYear(tempObject.getString(YEAR));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setGenre(tempObject.getString(GENRES));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {setPlot(tempObject.getString(PLOT));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setMovieRated(tempObject.getString(RATED));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setMovieRuntime(tempObject.getString(RUNTIME));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setMovieLang(tempObject.getString(LANG));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setMovieDirector(tempObject.getString(DIRECTOR));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setMovieActors(tempObject.getString(ACTORS));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setIconUrl(tempObject.getString(POSTER));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setImdbRating(tempObject.getString(IMDB_RATING));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {setImdbId(tempObject.getString(IMDB_ID));}
        catch (JSONException e) {
            e.printStackTrace();
        }

        setRating(0);
        this.movieEvents = new ArrayList<>();
    }



    public String getTitle() {
        return this.movieTitle;
    }

    public void setTitle(String title) {
        Log.i("MAD", title);
        this.movieTitle = title;
    }

    public String getYear() {
        return this.movieYear;
    }

    public void setYear(String year) {
        Log.i("MAD", "setYear");
        this.movieYear = year;
    }

    public String getGenre() {
        return this.movieGenre;
    }

    public void setGenre(String genre) {
        Log.i("MAD", "setGenre");
        this.movieGenre = genre;
    }

    public String getImdbId() {
        if (this.movieImdbId == null)
            return null;
        else
            return  this.movieImdbId;
    }

    public void setImdbId(String id) {
        if (id == null)
            return;
        else
            this.movieImdbId = id;
    }

    public String getImdbRating() {
        return this.movieImdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.movieImdbRating = imdbRating;
    }

    public float getMyRating() {
        return this.movieMyRating;
    }

    public void setRating(float r) {
        this.movieMyRating = r;
    }

    public String getIconUrl() {return this.movieImageURL;}
    public void setIconUrl(String iconUrl) {
        this.movieImageURL = iconUrl;
    }

    public String getPlot() {
        return this.moviePlot;
    }

    public void setPlot(String plot) {
        this.moviePlot = plot;
    }

    public Uri getIconUri(){
        Uri uri = Uri.parse(this.movieImageURL);
        return uri;
    }

    public String getMovieRuntime() {
        return movieRuntime;
    }

    public void setMovieRuntime(String movieRuntime) {
        this.movieRuntime = movieRuntime;
    }

    public String getMovieRated() {
        return movieRated;
    }

    public void setMovieRated(String movieRated) {
        this.movieRated = movieRated;
    }

    public String getMovieLang() {
        return movieLang;
    }

    public void setMovieLang(String movieLang) {
        this.movieLang = movieLang;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieActors() {
        return movieActors;
    }

    public void setMovieActors(String movieActors) {
        this.movieActors = movieActors;
    }

    public void addEvent(Event e) {
        this.movieEvents.add(e);
    }

    public List<Event> getEvents() {
        return this.movieEvents;
    }

    public Event getEventByID(String eventID) {
        String id;
        for(Event e: this.movieEvents) {
            id = e.getID();
            if(id.equals(eventID)) return e;
        }
        return null;
    }

    public boolean removeEvent(String eventID) {
        Event e;
        if((e = getEventByID(eventID)) == null) return false;
        this.movieEvents.remove(getEventByID(eventID));
        return true;
    }
}
