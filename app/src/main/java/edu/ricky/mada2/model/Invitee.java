package edu.ricky.mada2.model;

import java.io.Serializable;

/**
 * Created by Ricky Wu on 2015/8/27.
 */
public class Invitee implements Serializable {
    private String name;
    private String email;
    private String phone;
    public Invitee(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
