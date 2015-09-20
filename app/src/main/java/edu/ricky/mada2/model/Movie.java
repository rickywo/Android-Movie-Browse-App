package edu.ricky.mada2.model;

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

    /*private String movieTitle;
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
    private String movieImdbId;*/
    private JSONObject movieJson;

    // self-defined variablea
    private float movieMyRating; // Personal rating of this movie
    // List for holding event
    List<Event> movieEvents;

    public Movie(JSONObject tempObject) {
        this.movieJson = tempObject;
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

        try {
            setMyRating(tempObject.getDouble(MY_RATING));
            Log.e("Movie",String.valueOf(tempObject.getDouble(MY_RATING)) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.movieEvents = new ArrayList<>();
    }

    public String getJsonString() {
        return movieJson.toString();
    }

    public String getTitle() {
        try {
            return movieJson.getString(TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTitle(String title) {
        try {
            movieJson.put(TITLE, title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getYear() {
        try {
            return movieJson.getString(YEAR);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setYear(String year) {
        try {
            movieJson.put(YEAR, year);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGenre() {
        try {
            return movieJson.getString(GENRES);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setGenre(String genre) {
        try {
            movieJson.put(GENRES, genre);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImdbId() {
        try {
            return movieJson.getString(IMDB_ID);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setImdbId(String id) {
        try {
            movieJson.put(IMDB_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImdbRating() {
        try {
            return movieJson.getString(IMDB_RATING);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setImdbRating(String imdbRating) {
        try {
            movieJson.put(IMDB_RATING, imdbRating);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getMyRating() {
        try {
            return movieJson.getDouble(MY_RATING);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setMyRating(double r) {
        try {
            movieJson.put(MY_RATING, r);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIconUrl() {
        try {
            return movieJson.getString(POSTER);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setIconUrl(String iconUrl) {
        try {
            movieJson.put(POSTER, iconUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPlot() {
        try {
            return movieJson.getString(PLOT);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPlot(String plot) {
        try {
            movieJson.put(PLOT, plot);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMovieRuntime() {
        try {
            return movieJson.getString(RUNTIME);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMovieRuntime(String movieRuntime) {
        try {
            movieJson.put(RUNTIME, movieRuntime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMovieRated() {
        try {
            return movieJson.getString(RATED);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMovieRated(String movieRated) {
        try {
            movieJson.put(RATED, movieRated);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMovieLang() {
        try {
            return movieJson.getString(LANG);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMovieLang(String movieLang) {
        try {
            movieJson.put(LANG, movieLang);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMovieDirector() {
        try {
            return movieJson.getString(DIRECTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMovieDirector(String movieDirector) {
        try {
            movieJson.put(DIRECTOR, movieDirector);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMovieActors() {
        try {
            return movieJson.getString(ACTORS);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMovieActors(String movieActors) {
        try {
            movieJson.put(ACTORS, movieActors);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
