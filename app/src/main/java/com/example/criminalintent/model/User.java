package com.example.criminalintent.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "userTable")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUser")
    private long mPrimaryId;
    @ColumnInfo(name = "uuidUser")
    private UUID mIdUser;
    @ColumnInfo(name = "username")
    private String mName = " ";
    @ColumnInfo(name = "password")
    private String mPassword = " ";

    public User() {
    }

    public User(UUID id, String name, String password) {
        mIdUser = id;
        mName = name;
        mPassword = password;
    }

    public long getPrimaryId() {
        return mPrimaryId;
    }

    public void setPrimaryId(long primaryId) {
        mPrimaryId = primaryId;
    }

    public void setIdUser(UUID idUser) {
        mIdUser = idUser;
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
