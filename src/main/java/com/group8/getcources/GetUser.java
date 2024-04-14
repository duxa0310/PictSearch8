package com.group8.getcources;

import java.util.HashMap;

public class GetUser {
    public User getUser(String Name) {
        User user = new User(Name, new HashMap<>(), new Cource[3], new Cource[3]);
        return user;

    }

}
