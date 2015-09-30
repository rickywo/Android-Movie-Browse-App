package edu.ricky.mada2.model;

/**
 * Created by Ricky Wu on 2015/9/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by csimon on 12/11/13.
 */
public class UserDBModel extends SQLiteOpenHelper {

    private final static int DB_VERSION = 10;
    // Init Firebase db reference
    private Firebase ref = new Firebase("https://crackling-heat-3830.firebaseio.com");

    public UserDBModel(Context context) {
        // init local db
        super(context, "myApp.db", null, DB_VERSION);
        //
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table logins (userId Integer primary key autoincrement, " +
                " username text, password text)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            System.out.println("UPGRADE DB oldVersion=" + oldVersion + " - newVersion=" + newVersion);
            onCreate(sqLiteDatabase);
            if (oldVersion < 10) {
                String query = "create table logins (userId Integer primary key autoincrement, " +
                        " username text, password text)";
                sqLiteDatabase.execSQL(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // super.onDowngrade(db, oldVersion, newVersion);
        System.out.println("DOWNGRADE DB oldVersion=" + oldVersion + " - newVersion=" + newVersion);
    }

    public User insertUser(User queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", queryValues.username);
        values.put("password", queryValues.password);
        queryValues.userId = database.insert("logins", null, values);
        database.close();
        return queryValues;
    }

    public int updateUserPassword(User queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", queryValues.username);
        values.put("password", queryValues.password);
        queryValues.userId = database.insert("logins", null, values);
        database.close();
        return database.update("logins", values, "userId = ?", new String[]{String.valueOf(queryValues.userId)});
    }

    public User getUser(String username) {
        String query = "Select userId, password from logins where username ='" + username + "'";
        User myUser = new User(0, username, "");
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                myUser.userId = cursor.getLong(0);
                myUser.password = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        return myUser;
    }

    private void firebaseCreateUser(String uid, String pw) {
        ref.createUser(uid, pw, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d("firebaseCreateUser", "Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    private void firebaseLoginUser(String uid, String pw) {
        ref.authWithPassword(uid, pw, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("firebaseCreateUser", "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                // Something went wrong :(
                switch (firebaseError.getCode()) {
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        // handle a non existing user
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        // handle an invalid password
                        break;
                    case FirebaseError.NETWORK_ERROR:
                        // handle a network error
                        break;
                    default:
                        // handle other errors
                        break;
                }
            }
        });
    }

    private void firebaseLogoutUser() {
        ref.unauth();
    }
}