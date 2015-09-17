package edu.ricky.mada2;

import android.app.Application;
import android.content.Context;

import edu.ricky.mada2.model.User;
import edu.ricky.mada2.utility.NetworkStateManager;

public class MovieGangApp extends Application {

    // For detecting network connection
    private NetworkStateManager myStateManager;

    // Login Status
    private boolean login_state;
    // Current login user reference (null if login state == false)
    private User current_user;


    public void initStateManager(Context context){
        if(myStateManager == null) {
            myStateManager = new NetworkStateManager(context);
        }
    }

    /*public MovieGangApp() {
        super();
        setLoginState(false);
        setCurrentUser(null);
    }*/

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
