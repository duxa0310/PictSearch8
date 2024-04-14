package com.group8.getcources;

import java.util.HashMap;

public class User {
    String name;
    public HashMap<Cource, String> userCourcesAndProgress = new HashMap<>(); // дефорт для стрига -> 0
    public Cource[] doneCources = new Cource[100];
    //HashMap<String, String> doneCources = new HashMap<>();
    public Cource[] compareArray;
    //Cource[] suggestion = new Cource[100];
    public String required_price = "", required_rate = "", required_time = "";
    public User(String name,HashMap<Cource, String> userCourcesAndProgress,  Cource[] doneCources, Cource[] compareArray) {
        this.name = name;
        this.userCourcesAndProgress = userCourcesAndProgress;
        this.doneCources = doneCources;
        this.compareArray = compareArray;
    }
}
