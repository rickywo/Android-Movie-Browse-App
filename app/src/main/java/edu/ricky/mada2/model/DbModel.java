package edu.ricky.mada2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import edu.ricky.mada2.utility.BoundedLruCache;

/**
 * Created by Ricky Wu on 2015/9/5.
 */
public class DbModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    // Movie table column mapping
    private static final int IMDB_ID_COLUMN = 0;
    private static final int JSONSTRING_COLUMN = 1;
    private static final int ORDER_COLUMN = 2;
    private static final String ID = "id";
    private static final String JSON = "json";
    private static final String ORDER = "order";

    // Event table mapping
    private static final int ID_COLUMN = 1;
    private static final int MOVIE_ID_FKEY_COLUMN = 0;
    private static final int LOC_COLUMN = 0;
    private static final int DATE_COLUMN = 0;
    private static final String DATABASE_NAME = "MovieGangDB";

    private static DbModel singleton = null;

    public static DbModel getSingleton(Context context)
    {
        if(singleton == null)
            singleton = new DbModel(context);
        return singleton;
    }

    private static final String CREATE_MOVIE_TABLE =
            "CREATE TABLE IF NOT EXISTS movies (" +
                    "id TEXT PRIMARY KEY NOT NULL," +
                    "json VARCHAR NOT NULL," +
                    "\"order\" INTEGER )" ;

    private static final String CREATE_EVENT_TABLE =
            "CREATE TABLE IF NOT EXISTS events (" +
                    "id TEXT PRIMARY KEY NOT NULL," +
                    "json VARCHAR NOT NULL )" ;

    private static final String TABLE_MOVIES = "movies";
    private static final String TABLE_EVENTS = "events";

    private DbModel(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

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

    /**
     * Get all movie records from sqlite db
     *
     * @return Map object, entry <movie id string, movie JSONobject string>
     */
    public Map<String, String> getAllMovies() {
        Map<String, String> movieJsonMap = new BoundedLruCache<>(10);


        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MOVIES + " ORDER BY " + "\"" + ORDER + "\"";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
                movieJsonMap.put(cursor.getString(IMDB_ID_COLUMN), cursor.getString(JSONSTRING_COLUMN));
            } while (cursor.moveToNext());
        }

        // return movieMap
        return movieJsonMap;
    }

    //SELECT * FROM Orders WHERE Id=6;
    /**
     * Get movie records from sqlite db with id provided
     *
     * @return Map object, entry <movie id string, movie JSONobject string>
     */
    public Map<String, String> getMovieByID(String id) {
        Map<String, String> movieJsonMap = new HashMap<>();


        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MOVIES + " WHERE " + ID + " = " + "\""+id+"\"";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
                movieJsonMap.put(cursor.getString(IMDB_ID_COLUMN), cursor.getString(JSONSTRING_COLUMN));
            } while (cursor.moveToNext());
        }

        // return movieMap
        return movieJsonMap;
    }

    /**
     * insert movie records to sqlite db
     *
     * @return Map object, entry <movie id string, movie JSONobject string>
     */
    public boolean insertMovie(Movie movie) {
        // TODO: Save all movie into sqlite db iteratively

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. insert movie in db
        try {
            ContentValues values = new ContentValues();
            values.put("id", movie.getImdbId());
            values.put("json", movie.getJsonString());
            db.insertWithOnConflict(TABLE_MOVIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean insertMovie(Movie movie, int order) {
        return insertMovie(movie);

    }

    /**
     *
     * @param movieMap entry: <movie id string, movie json string>
     */
    public void saveAllMovies(BoundedLruCache<String, Movie> movieMap) {
        // TODO: Save all movie into sqlite db iteratively

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        int i = 0;
        // 3. go over each row, build movies and add it to list
        for (Map.Entry<String, Movie> entry : movieMap.entrySet()) {
            insertMovie(entry.getValue(), i);
            i ++;
        }
    }

    /**
     * Get all event records from sqlite db
     *
     * @return Map object, entry <event id string, event JSONobject string>
     */
    public Map<String, String> getAllEvents() {
        // For Local Db
        Map<String, String> eventJsonMap = new HashMap<>();


        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_EVENTS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
                eventJsonMap.put(cursor.getString(IMDB_ID_COLUMN), cursor.getString(JSONSTRING_COLUMN));
            } while (cursor.moveToNext());
        }

        // return movieMap
        return eventJsonMap;
    }

    /**
     * TODO: Save all movie into sqlite db iteratively
     *
     * @param eventMap entry: <movie id string, movie json string>
     */
    /*public void saveAllEvents(Map<String, Event> eventMap) {

        // For local db
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. clear all data from table
        try {
            db.execSQL("delete from "+ TABLE_EVENTS);
        } catch (Exception e) {}

        // 3. go over each row, build movies and add it to list
        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            try{
                ContentValues values = new ContentValues();
                values.put("id", entry.getKey());
                values.put("json", (entry.getValue()).toString());
                Log.e("saveAllEvents", (entry.getValue()).toString());
                db.insert(TABLE_EVENTS, null, values);
            } catch (Exception e) {
            }
        }
    }*/
}

