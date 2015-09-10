package edu.ricky.mada2.model;

import java.io.Serializable;

/**
 * Created by Ricky Wu on 2015/8/27.
 */
public class Invitee implements Serializable {
    private String name;
    private String phone;
    public Invitee(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
