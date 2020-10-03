package com.example.criminalintent.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.criminalintent.utils.DateUtils;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "crimeTable")
public class Crime {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mPrimaryId;
    @ColumnInfo(name = "uuid")
    private UUID mId;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo (name = "date")
    private Date mDate;
    @ColumnInfo(name = "solved")
    private boolean mSolved;
    @ColumnInfo(name = "suspect")
    private String mSuspect;


    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public long getPrimaryId() {
        return mPrimaryId;
    }

    public void setPrimaryId(long primaryId) {
        mPrimaryId = primaryId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Crime() {
        this(UUID.randomUUID());
//        mDate = DateUtils.randomDate();
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public Crime(UUID id, String title, Date date, boolean solved, String suspect) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
        mSuspect = suspect;
    }

}
