package edu.ricky.mada2.model;

import android.util.Log;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ricky.mada2.utility.BoundedLruCache;

/**
 * Created by Ricky Wu on 2015/8/26.
 */
public class MovieModel {
    // Singleton
    private static MovieModel singleton = null;

    // Model
    private Map<String, Movie> movieMap;

    public static MovieModel getSingleton()
    {
        if(singleton == null)
            singleton = new MovieModel();
        return singleton;
    }



    // Construction
    private MovieModel()
    {
        this.movieMap = new BoundedLruCache<>(10);
    }

    // Model Access
    public Movie getMovieById(String imdbId)
    {
        return movieMap.get(imdbId);
    }

    public boolean contains(String imdbId) {
        return this.movieMap.containsKey(imdbId);
    }

    public Map<String, Movie> getMovieMap() {
        return movieMap;
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
        Log.e("addMovie", movie.toString());
        this.movieMap.put(movie.getImdbId(), movie);
        return true;
    }

    public boolean updateMovie(Movie movie) {
        if(contains(movie.getImdbId())) {
            this.movieMap.remove(movie.getImdbId());
            this.movieMap.put(movie.getImdbId(), movie);
            return true;
        }
        return false;
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

    public void close() {
    }
}
