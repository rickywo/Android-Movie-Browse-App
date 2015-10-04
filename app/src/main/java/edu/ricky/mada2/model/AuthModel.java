package edu.ricky.mada2.model;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Ricky Wu on 2015/10/4.
 */
public class AuthModel {
    // Init Firebase db reference
    private Firebase ref = new Firebase("https://crackling-heat-3830.firebaseio.com");
    private User currentUser = new User();
    private AuthModel() {

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
