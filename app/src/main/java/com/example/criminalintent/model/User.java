package com.example.criminalintent.model;

import java.util.UUID;

public class User {
    private UUID mIdUser;
    private String mName=" ";
    private String mPassword=" ";

    public User() {
    }

    public User(UUID id, String name, String password) {
        mIdUser = id;
        mName = name;
        mPassword = password;
    }

    public UUID getIdUser() {
        return mIdUser;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
