package edu.ricky.mada2.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import edu.ricky.mada2.model.Movie;

/**
 * Created by Ricky Wu on 2015/9/5.
 */
public class DbController extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Movie table column mapping
    private static final int IMDB_ID_COLUMN = 0;
    private static final int TITLE_COLUMN = 1;
    private static final int YEAR_COLUMN = 2;
    private static final int GENRE_COLUMN = 3;
    private static final int IMG_URL_COLUMN = 4;
    private static final int SHORT_PLOT_COLUMN = 5;
    private static final int LONG_PLOT_COLUMN = 6;
    private static final int IMDB_RATING_COLUMN = 7;
    private static final int MY_RATING_COLUMN = 8;

    // Event table mapping
    private static final int ID_COLUMN = 1;
    private static final int MOVIE_ID_FKEY_COLUMN = 0;
    private static final int LOC_COLUMN = 0;
    private static final int DATE_COLUMN = 0;
    private static final String DATABASE_NAME = "MovieClubDB";

    private static final String CREATE_MOVIE_TABLE =
            "CREATE TABLE movies (" +
                    "id TEXT PRIMARY KEY NOT NULL" +
                    "json VARCHAR NOT NULL" ;

    private static final String CREATE_EVENT_TABLE =
            "CREATE TABLE events (" +
                    "id TEXT PRIMARY KEY NOT NULL" +
                    "movie_id TEXT REFERENCES movies (id) NOT NULL" +
                    "loc TEXT" +
                    "date TEXT )";

    private static final String TABLE_MOVIES = "movies";
    private static final String TABLE_EVENTS = "events";

    public DbController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create movies table
        db.execSQL(CREATE_MOVIE_TABLE);

        // create events table
        db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS movies");
        db.execSQL("DROP TABLE IF EXISTS events");
        // create fresh books table
        this.onCreate(db);

    }

    // Get All movies
    public Map<String, Movie> getAllMovies() {
        Map<String, Movie> movieMap = new HashMap<>();


        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MOVIES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Map<String, String> movie = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                movie.put(Movie.IMDB_ID, cursor.getString(IMDB_ID_COLUMN));
                movie.put(Movie.TITLE, cursor.getString(TITLE_COLUMN));
                movie.put(Movie.YEAR, cursor.getString(YEAR_COLUMN));
                movie.put(Movie.GENRES, cursor.getString(GENRE_COLUMN));
                movie.put(Movie.PLOT, cursor.getString(SHORT_PLOT_COLUMN));
                movie.put(Movie.POSTER, cursor.getString(IMG_URL_COLUMN));
                movie.put(Movie.IMDB_RATING, cursor.getString(IMDB_RATING_COLUMN));
                movie.put(Movie.MY_RATING, cursor.getString(MY_RATING_COLUMN));
                Movie m = new Movie(new JSONObject(movie));
                // Add book to books
                movieMap.put(m.getImdbId(), m);
            } while (cursor.moveToNext());
        }

        // return movieMap
        return movieMap;
    }

    // Save All movies
    public Map<String, Movie> saveAllMovies() {
        Map<String, Movie> movieMap = new HashMap<>();


        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MOVIES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Map<String, String> movie = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                movie.put(Movie.IMDB_ID, cursor.getString(IMDB_ID_COLUMN));
                movie.put(Movie.TITLE, cursor.getString(TITLE_COLUMN));
                movie.put(Movie.YEAR, cursor.getString(YEAR_COLUMN));
                movie.put(Movie.GENRES, cursor.getString(GENRE_COLUMN));
                movie.put(Movie.PLOT, cursor.getString(SHORT_PLOT_COLUMN));
                movie.put(Movie.POSTER, cursor.getString(IMG_URL_COLUMN));
                movie.put(Movie.IMDB_RATING, cursor.getString(IMDB_RATING_COLUMN));
                movie.put(Movie.MY_RATING, cursor.getString(MY_RATING_COLUMN));
                Movie m = new Movie(new JSONObject(movie));
                // Add book to books
                movieMap.put(m.getImdbId(), m);
            } while (cursor.moveToNext());
        }

        // return movieMap
        return movieMap;
    }
}

