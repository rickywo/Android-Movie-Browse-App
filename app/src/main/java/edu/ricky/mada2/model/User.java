package edu.ricky.mada2.model;

/**
 * Created by Ricky Wu on 2015/9/16.
 */
public class User {
    public long userId;
    public String username;
    public String password;

    public User(long userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

}