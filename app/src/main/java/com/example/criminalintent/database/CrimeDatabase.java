package com.example.criminalintent.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.User;

@Database(entities = {Crime.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CrimeDatabase extends RoomDatabase {

    public abstract CrimeDAO getCrimeTable();

    public abstract UserDAO getUserTable();
}
