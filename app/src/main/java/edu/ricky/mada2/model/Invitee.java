package edu.ricky.mada2.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Ricky Wu on 2015/8/27.
 */
public class Invitee implements Serializable {
    private static  String NAME = "name";
    private static String EMAIL = "email";
    private static String PHONE = "phone";
    private String name;
    private String email;
    private String phone;
    public Invitee(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Invitee(JSONObject jsonObject) {
        try {
            setName(jsonObject.getString(NAME));}
        catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setEmail(jsonObject.getString(EMAIL));}
        catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setPhone(jsonObject.getString(PHONE));}
        catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        HashMap<String, String> valueMap = new HashMap<>();
        valueMap.put(NAME, getName());
        valueMap.put(EMAIL, getEmail());
        valueMap.put(PHONE, getPhone());
        return new JSONObject(valueMap).toString();
    }
}
