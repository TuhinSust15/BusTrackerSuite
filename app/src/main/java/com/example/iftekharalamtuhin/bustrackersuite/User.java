package com.example.iftekharalamtuhin.bustrackersuite;

/**
 * Created by Iftekhar Alam Tuhin on 18-Jul-18.
 */

public class User {

    public String email,busType,busRoute;

    public User(){


    }

    public User(String email, String busType, String busRoute) {
        this.email = email;
        this.busType = busType;
        this.busRoute = busRoute;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }
}
