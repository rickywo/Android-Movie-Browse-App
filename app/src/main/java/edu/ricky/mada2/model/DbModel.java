package edu.ricky.mada2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ricky Wu on 2015/9/5.
 */
public class DbModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Movie table column mapping
    private static final int IMDB_ID_COLUMN = 0;
    private static final int JSONSTRING_COLUMN = 1;

    // Event table mapping
    private static final int ID_COLUMN = 1;
    private static final int MOVIE_ID_FKEY_COLUMN = 0;
    private static final int LOC_COLUMN = 0;
    private static final int DATE_COLUMN = 0;
    private static final String DATABASE_NAME = "MovieClubDB";

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
                    "json VARCHAR NOT NULL )" ;

    private static final String CREATE_EVENT_TABLE =
            "CREATE TABLE IF NOT EXISTS events (" +
                    "id TEXT PRIMARY KEY NOT NULL" +
                    "movie_id TEXT REFERENCES movies (id) NOT NULL" +
                    "loc TEXT" +
                    "date TEXT )";

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
        //db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS movies");
        //db.execSQL("DROP TABLE IF EXISTS events");
        // create fresh books table
        this.onCreate(db);

    }

    // Get All movies
    public Map<String, String> getAllMovies() {
        Map<String, String> movieJsonMap = new HashMap<>();


        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MOVIES;

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

    // Save All movies
    public void saveAllMovies(Map<String, Movie> movieMap) {


        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. clear all data from table
        db.execSQL("delete from "+ TABLE_MOVIES);

        // 3. go over each row, build movies and add it to list
        for (Map.Entry<String, Movie> entry : movieMap.entrySet()) {
            try{
                ContentValues values = new ContentValues();
                values.put("id", entry.getKey());
                values.put("json", (entry.getValue()).getJsonString());
                db.insert(TABLE_MOVIES, null, values);
            } catch (Exception e) {
            }
        }
    }
}

