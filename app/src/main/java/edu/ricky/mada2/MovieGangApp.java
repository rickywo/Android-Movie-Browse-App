package edu.ricky.mada2;

import android.app.Application;

import edu.ricky.mada2.utility.NetworkStateManager;

public class MovieGangApp extends Application {

    private NetworkStateManager myStateManager = new NetworkStateManager();

    public NetworkStateManager getStateManager(){
        return myStateManager ;
    }
}
