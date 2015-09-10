package edu.ricky.mada2.model;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ricky Wu on 2015/8/26.
 */
public class MovieModel {
    // Singleton
    private static MovieModel singleton = null;

    // Model
    private Map<String, Movie> movieMap;
    private DbModel db;

    public static MovieModel getSingleton()
    {
        if(singleton == null)
            singleton = new MovieModel();
        return singleton;
    }



    // Construction
    private MovieModel()
    {
        this.movieMap = new HashMap<>();
        this.db = DbModel.getSingleton(null);
        load();
        /*for(String s: MovieSamples.mvJsons) {
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(s);
                //Movie m = new Movie(jsonObj);
                addMovie(jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    // Model Access
    public Movie getMovieById(String imdbId)
    {
        return movieMap.get(imdbId);
    }

    public boolean contains(String imdbId) {
        return this.movieMap.containsKey(imdbId);
    }

    public List<Movie> getAllMovies()
    {

        return new ArrayList(movieMap.values());
    }

    public boolean addMovie(JSONObject j) {
        Movie m = new Movie(j);
        if(contains(m.getImdbId())) {
            return false;
        }

        this.movieMap.put(m.getImdbId(), m);
        return true;
    }

    public boolean addMovie(Movie movie) {
        if(contains(movie.getImdbId())) {
            return false;
        }

        this.movieMap.put(movie.getImdbId(), movie);
        return true;
    }

    public boolean removeMovie(Movie m) {
        boolean result = true;
        if(!movieMap.containsKey(m.getImdbId())) {
            result = false;
        } else {
            movieMap.remove(m.getImdbId());
        }
        return result;
    }

    public int getMovieCount() {
        return movieMap.size();
    }

    public void save() {
        //TODO: Save Movies in the Map into Sqlite
        db.saveAllMovies(this.movieMap);
        return;
    }

    public void load() {
        //TODO: Load movies from Sqlite to Map
        Map<String, String> map = db.getAllMovies();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                Log.e("MovieModel", "load");
                JSONObject jo = new JSONObject(entry.getValue());
                Movie m = new Movie(jo);
                addMovie(m);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public void close() {
        save();
        db.close();
    }
}
