package edu.ricky.mada2;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import com.firebase.client.Firebase;

import edu.ricky.mada2.model.BitmapLruCache;
import edu.ricky.mada2.model.EventModel;
import edu.ricky.mada2.model.MovieModel;
import edu.ricky.mada2.model.User;
import edu.ricky.mada2.utility.NetworkStateManager;

public class MovieGangApp extends Application {

    public EventModel eventModel;
    public MovieModel movieModel;
    public BitmapLruCache bitmapLruCache;
    // For detecting network connection
    private NetworkStateManager myStateManager;


    // Login Status
    private boolean login_state;
    // Current login user reference (null if login state == false)
    private User current_user;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // init models for this app
        eventModel = EventModel.getSingleton();
        movieModel = MovieModel.getSingleton(getApplicationContext());
        bitmapLruCache = BitmapLruCache.getSingleton();

        // other setup code
    }

    public void initStateManager(Context context){
        if(myStateManager == null) {
            myStateManager = new NetworkStateManager(context);
        }
    }


    public boolean isConnected() {
        return myStateManager.isConnected;
    }

    public boolean isLoginState() {
        return login_state;
    }

    public void setLoginState(boolean loginState) {
        this.login_state = loginState;
    }

    public User getCurrentUser() {
        return current_user;
    }

    public void setCurrentUser(User currentUser) {
        this.current_user = currentUser;
    }

    public void login(User user) {
        setCurrentUser(user);
        setLoginState(true);
    }

    public void logout() {
        setCurrentUser(null);
        setLoginState(false);
    }


}
